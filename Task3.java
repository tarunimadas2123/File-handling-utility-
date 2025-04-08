import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class RecommendationSystem extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        try {
            File file = copyAssetToFile("data.csv");
            DataModel model = new FileDataModel(file);
            UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);
            UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

            long userId = 1;
            int numRecommendations = 3;

            List<RecommendedItem> recommendations = recommender.recommend(userId, numRecommendations);

            if (recommendations != null && !recommendations.isEmpty()) {
                StringBuilder sb = new StringBuilder("Recommendations for user " + userId + ":\n");
                for (RecommendedItem recommendation : recommendations) {
                    sb.append("Item ID: ").append(recommendation.getItemID()).append(", Value: ").append(recommendation.getValue()).append("\n");
                }
                textView.setText(sb.toString());
            } else {
                textView.setText("No recommendations found for user " + userId);
            }
        } catch (IOException e) {
            Log.e("Error", "Error reading data file: " + e.getMessage());
        } catch (TasteException e) {
            Log.e("Error", "Error generating recommendations: " + e.getMessage());
        } catch (Exception e) {
            Log.e("Error", "An error occurred: " + e.getMessage());
        }
    }

    private File copyAssetToFile(String filename) throws IOException {
        InputStream inputStream = getAssets().open(filename);
        File file = new File(getFilesDir(), filename);
        FileOutputStream outputStream = new FileOutputStream(file);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        inputStream.close();
        outputStream.close();

        return file;
    }
}
