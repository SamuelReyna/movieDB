package SRMovieDB.Controller;

import SRMovieDB.ML.DetailML;
import SRMovieDB.ML.MovieML;
import SRMovieDB.ML.MovieResponse;
import SRMovieDB.ML.UserML;
import SRMovieDB.Service.MovieService;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("movie")
public class MovieController {

    private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwOTg2ZmY4NzM0Y2YyMDI4NWFkNzBiNjJlODZmYzY0NSIsIm5iZiI6MTc1NzcwMzIyNC45NTcsInN1YiI6IjY4YzQ2YzM4YTlmYjViODI3NjhkNzNjYiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.H8LWQFVeOAKNZZ00HhZCIYChFfrsT3_paRZZpevGlu8";

    @Autowired
    private MovieService movieService;

    @GetMapping()
    public String Index(Model model) {
        String session_id = movieService.getSessionId();
        if (session_id != null) {

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);

            HttpEntity<String> requestEntity = new HttpEntity(headers);
            //get account id
            ResponseEntity<DetailML> sessionResponse
                    = restTemplate.exchange(
                            "https://api.themoviedb.org/3/account?session_id=" + session_id,
                            HttpMethod.GET,
                            requestEntity,
                            new ParameterizedTypeReference<DetailML>() {
                    });
            DetailML details = sessionResponse.getBody();
            movieService.setAccountId(details.getId());
            model.addAttribute("AccountId", movieService.getAccountId());
            model.addAttribute("token", token);
            //get most popular movies
            ResponseEntity<MovieResponse> response
                    = restTemplate.exchange(
                            "https://api.themoviedb.org/3/movie/popular?language=es-MX",
                            HttpMethod.GET,
                            requestEntity,
                            new ParameterizedTypeReference<MovieResponse>() {
                    });
            MovieResponse body = response.getBody();
            List<MovieML> movies = body.getResults();

            //get favorites movies
            ResponseEntity<MovieResponse> responseFavorites
                    = restTemplate.exchange(
                            "https://api.themoviedb.org/3/account/" + movieService.getAccountId() + "/favorite/movies?language=es-MX",
                            HttpMethod.GET,
                            requestEntity,
                            new ParameterizedTypeReference<MovieResponse>() {
                    });
            MovieResponse favoriteBody = responseFavorites.getBody();
            List<MovieML> favoriteMovies = favoriteBody.getResults();

            Set<Integer> favoriteIds = favoriteMovies.stream()
                    .map(MovieML::getId)
                    .collect(Collectors.toSet());

            for (MovieML movie : movies) {
                if (favoriteIds.contains(movie.getId())) {
                    movie.setFavorite(true);
                }
            }

            model.addAttribute("movies", movies);

            return "Index";
        }
        return "redirect:/movie/login";
    }

    @GetMapping("/login")
    public String Login(@ModelAttribute("user") UserML user) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<String> requestEntity = new HttpEntity(headers);

        ResponseEntity<HashMap> responseEntity
                = restTemplate.exchange("https://api.themoviedb.org/3/authentication/token/new", HttpMethod.GET,
                        requestEntity,
                        HashMap.class);
        HashMap<String, Object> body = responseEntity.getBody();

        if (body != null && Boolean.TRUE.equals(body.get("success"))) {
            String requestToken = (String) body.get("request_token");
            movieService.setRequestToken(requestToken);

        }
        return "Login";
    }

    @PostMapping("/auth")
    public String Auth(@ModelAttribute("user") UserML user) {
        String requestToken = movieService.getRequestToken();
        if (requestToken != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);
            HashMap<String, Object> body = new HashMap();
            body.put("username", user.getUsername());
            body.put("password", user.getPassword());
            body.put("request_token", requestToken);
            HttpEntity<String> requestEntity = new HttpEntity(body, headers);

            ResponseEntity<HashMap> responseEntity
                    = restTemplate.exchange("https://api.themoviedb.org/3/authentication/token/validate_with_login",
                            HttpMethod.POST,
                            requestEntity,
                            HashMap.class);
            HashMap<String, Object> responseBody;
            responseBody = responseEntity.getBody();
            if (responseBody != null && Boolean.TRUE.equals(responseBody.get("success"))) {
                HashMap<String, Object> bodySesion = new HashMap<>();
                bodySesion.put("request_token", requestToken);
                HttpEntity<String> requestSession = new HttpEntity(bodySesion, headers);

                ResponseEntity<HashMap> sessionResponse
                        = restTemplate.exchange("https://api.themoviedb.org/3/authentication/session/new",
                                HttpMethod.POST,
                                requestSession,
                                HashMap.class
                        );
                HashMap<String, Object> sessionBody = sessionResponse.getBody();
                if (sessionBody != null && Boolean.TRUE.equals(sessionBody.get("success"))) {
                    String sessionId = (String) sessionBody.get("session_id");
                    movieService.setSessionId(sessionId);
                }
            }
        }
        return "redirect:/movie";
    }

    @GetMapping("/logout")
    public String Logout() {

        RestTemplate restTemplate = new RestTemplate();
        String SessionId = movieService.getSessionId();

        if (SessionId != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);
            HashMap<String, Object> body = new HashMap<>();
            body.put("session_id", SessionId);
            HttpEntity<String> requestEntity = new HttpEntity(body, headers);

            ResponseEntity<HashMap> response
                    = restTemplate.exchange("https://api.themoviedb.org/3/authentication/session",
                            HttpMethod.DELETE, requestEntity, HashMap.class);
            HashMap<String, Object> responseBody = response.getBody();

            if (response != null && Boolean.TRUE.equals(responseBody.get("success"))) {
                movieService.clearSession();
                return "redirect:/movie/login";
            }
        }
        return "redirect:/movie";
    }

    @GetMapping("/profile/details")
    public String AccountDetail(Model model) {
        String session_id = movieService.getSessionId();
        if (session_id != null) {

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);

            HttpEntity<String> requestEntity = new HttpEntity(headers);
            ResponseEntity<DetailML> sessionResponse
                    = restTemplate.exchange(
                            "https://api.themoviedb.org/3/account?session_id=" + session_id,
                            HttpMethod.GET,
                            requestEntity,
                            new ParameterizedTypeReference<DetailML>() {
                    });
            DetailML details = sessionResponse.getBody();
            model.addAttribute("details", details);

        }
        return "details";
    }

    @GetMapping("/profile/favorites")
    public String Favorites(Model model) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<String> requestEntity = new HttpEntity(headers);
        long AccountId = movieService.getAccountId();
        ResponseEntity<MovieResponse> response
                = restTemplate.exchange(
                        "https://api.themoviedb.org/3/account/" + AccountId + "/favorite/movies?language=es-MX",
                        HttpMethod.GET,
                        requestEntity,
                        new ParameterizedTypeReference<MovieResponse>() {
                });
        MovieResponse body = response.getBody();
        List<MovieML> movies = body.getResults();
        for (MovieML movie : movies) {
            movie.setFavorite(true);
        }
        model.addAttribute("AccountId", movieService.getAccountId());
        model.addAttribute("token", token);
        model.addAttribute("movies", movies);
        return "favorites";
    }
}
