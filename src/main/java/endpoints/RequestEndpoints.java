package endpoints;

import io.restassured.response.Response;
import pages.AddBooksByISBNPage;
import pages.UpdateBooksPage;
import pages.UserDetails;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.with;

public class RequestEndpoints {

public static Response postData(UserDetails userDetails, String endpoint){
    return  with()
            .body(userDetails)
            .post(endpoint);
}

public static Response postData(AddBooksByISBNPage addBooksByISBNPageRequestBody, String endpoint, String token){
        return  with()
                .header("Authorization", "Bearer " + token)
                .body(addBooksByISBNPageRequestBody)
                .post(endpoint);
    }

public static Response getData(String endpoint){
    return get(endpoint);
}

    public static Response getData(String endpoint, String ISBN){
        return with()
                .queryParam("ISBN",ISBN)
                .get(endpoint);
    }

    public static Response updateBookDetails(UpdateBooksPage updateBooksPageReq, String endpoint, String token, String ISBN){
       return with()
                .header("Authorization","Bearer "+token)
                .pathParam( "ISBN","9781449325862")
                .body(updateBooksPageReq)
                .put(endpoint+"{ISBN}");
    }

    public static Response deleteBook(UpdateBooksPage updateBooksPageReq,String endpoint, String token){
     return  with()
                .header("Authorization","Bearer "+token)
                .body(updateBooksPageReq)
                .delete(endpoint);
    }

}
