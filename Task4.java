import java.util.*;

public class RecommendationEngine {

    // Sample Data: User Preferences (Movie Ratings)
    private static Map<String, Map<String, Double>> userRatings = new HashMap<>();

    static {
        // User 1 ratings
        Map<String, Double> user1Ratings = new HashMap<>();
        user1Ratings.put("Movie A", 4.5);
        user1Ratings.put("Movie B", 3.0);
        user1Ratings.put("Movie C", 5.0);
        userRatings.put("User 1", user1Ratings);

        // User 2 ratings
        Map<String, Double> user2Ratings = new HashMap<>();
        user2Ratings.put("Movie A", 3.0);
        user2Ratings.put("Movie B", 4.0);
        user2Ratings.put("Movie D", 4.5);
        userRatings.put("User 2", user2Ratings);

        // User 3 ratings
        Map<String, Double> user3Ratings = new HashMap<>();
        user3Ratings.put("Movie C", 4.0);
        user3Ratings.put("Movie D", 5.0);
        user3Ratings.put("Movie E", 3.5);
        userRatings.put("User 3", user3Ratings);

        //User 4 ratings
        Map<String, Double> user4Ratings = new HashMap<>();
        user4Ratings.put("Movie A", 5.0);
        user4Ratings.put("Movie E", 2.0);
        user4Ratings.put("Movie B", 1.0);
        userRatings.put("User 4", user4Ratings);
    }

    // Calculate similarity between two users using Pearson Correlation
    public static double calculateSimilarity(Map<String, Double> user1Ratings, Map<String, Double> user2Ratings) {
        Set<String> commonMovies = new HashSet<>(user1Ratings.keySet());
        commonMovies.retainAll(user2Ratings.keySet());

        if (commonMovies.isEmpty()) {
            return 0.0; // No common movies, no similarity
        }

        double sum1 = 0.0, sum2 = 0.0, sum1Sq = 0.0, sum2Sq = 0.0, pSum = 0.0;
        int n = commonMovies.size();

        for (String movie : commonMovies) {
            double rating1 = user1Ratings.get(movie);
            double rating2 = user2Ratings.get(movie);
            sum1 += rating1;
            sum2 += rating2;
            sum1Sq += rating1 * rating1;
            sum2Sq += rating2 * rating2;
            pSum += rating1 * rating2;
        }

        double numerator = pSum - (sum1 * sum2 / n);
        double denominator = Math.sqrt((sum1Sq - Math.pow(sum1, 2) / n) * (sum2Sq - Math.pow(sum2, 2) / n));

        if (denominator == 0) {
            return 0.0; // Avoid division by zero
        }
        return numerator / denominator;
    }

    // Generate recommendations for a user
    public static Map<String, Double> generateRecommendations(String targetUser) {
        Map<String, Double> targetUserRatings = userRatings.get(targetUser);
        if (targetUserRatings == null) {
            return new HashMap<>(); // User not found
        }

        Map<String, Double> recommendations = new HashMap<>();
        for (String otherUser : userRatings.keySet()) {
            if (!otherUser.equals(targetUser)) {
                double similarity = calculateSimilarity(targetUserRatings, userRatings.get(otherUser));
                if (similarity > 0) { // Consider only positive similarities
                    for (String movie : userRatings.get(otherUser).keySet()) {
                        if (!targetUserRatings.containsKey(movie)) { // Recommend movies the target user hasn't seen
                            recommendations.merge(movie, similarity * userRatings.get(otherUser).get(movie), Double::sum);
                        }
                    }
                }
            }
        }
        // Normalize recommendations (Optional, for better results)
        double maxRecommendation = 0;
        for(double value : recommendations.values()){
            if(value > maxRecommendation){
                maxRecommendation = value;
            }
        }
        if (maxRecommendation > 0){
            for(String movie: recommendations.keySet()){
                recommendations.put(movie, recommendations.get(movie)/maxRecommendation);
            }
        }
        return recommendations;
    }

    public static void main(String[] args) {
        String targetUser = "User 1";
        Map<String, Double> recommendations = generateRecommendations(targetUser);

        System.out.println("Recommendations for " + targetUser + ":");
        recommendations.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));

        String targetUser2 = "User 4";
        Map<String, Double> recommendations2 = generateRecommendations(targetUser2);

        System.out.println("\nRecommendations for " + targetUser2 + ":");
        recommendations2.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
    }
}
