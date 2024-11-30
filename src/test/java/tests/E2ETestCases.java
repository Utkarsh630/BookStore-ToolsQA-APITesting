package tests;

import base.BaseTest;
import config.ConfigLoader;
import endpoints.RequestEndpoints;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


public class E2ETestCases extends BaseTest {

    @Test ( description = "User Registration API with authentication: Validate Response status, Headers, and Response Body" )
    public void registerUserTest ( ) {
        Response response = RequestEndpoints.postData ( userDetails , ConfigLoader.getProperty ( "registerUser" ) )
                .then ( )
                .statusCode ( HttpStatus.SC_CREATED )
                .body ( matchesJsonSchemaInClasspath ( "schemas/RegisterJsonSchema.json" ) ).extract ( ).response ( );
        RegisterResponse registerResponse = response.as ( RegisterResponse.class );
        userID = registerResponse.getUserID ( );
    }

    @Test ( priority = 1, description = "check for existing user registration" )
    public void userAlreadyExistsTest ( ) {
        RequestEndpoints.postData ( userDetails , ConfigLoader.getProperty ( "registerUser" ) )
                .then ( )
                .statusCode ( HttpStatus.SC_NOT_ACCEPTABLE )
                .body ( "code" , equalTo ( "1204" ) )
                .body ( "message" , equalTo ( "User exists!" ) );
    }

    @Test ( priority = 2, description = "Generate Token: Success- Validate Response Status, Headers, and Response Body" )
    public void generateTokenTest_Success ( ) {
        Response response = RequestEndpoints.postData ( userDetails , ConfigLoader.getProperty ( "generateToken" ) )
                .then ( )
                .statusCode ( HttpStatus.SC_OK )
                .body ( "token" , notNullValue ( ) )
                .body ( "expires" , notNullValue ( ) )
                .body ( "status" , equalTo ( "Success" ) )
                .body ( "result" , equalTo ( "User authorized successfully." ) ).extract ( ).response ( );
        GenerateTokenResponse generateTokenResponse = response.as ( GenerateTokenResponse.class );
        token = generateTokenResponse.getToken ( );
    }

    @Test ( priority = 3, description = "Get All Books: Validate Response Status, Headers, and Response Body" )
    public void getAllBooksTest ( ) {
        Response response = RequestEndpoints.getData ( ConfigLoader.getProperty ( "getBooks" ) )
                .then ( )
                .statusCode ( HttpStatus.SC_OK )
                .body ( "books" , notNullValue ( ) )
                .body ( matchesJsonSchemaInClasspath ( "schemas/Book.json" ) ).extract ( ).response ( );


    }

    @Test ( priority = 4, description = "Get Book by ISBN: Validate Response Status, Headers, and Response Body" )
    public void getBookByISBNTest ( ) {
        RequestEndpoints.getData ( ConfigLoader.getProperty ( "getBooks" ) , "9781449325862" )
                .then ( )
                .statusCode ( HttpStatus.SC_OK );
    }

    @Test ( priority = 5, description = "Add list of Books into user's book collection: Validate Response Status, Headers, and Response Body", dependsOnMethods = { "registerUserTest" , "generateTokenTest_Success" } )
    public void addListOfBooksTest ( ) {
        List < CollectionOfISBNs > collectionOfIsbns = new ArrayList <> ( );
        collectionOfIsbns.add ( new CollectionOfISBNs ( "9781449325862" ) );
        collectionOfIsbns.add ( new CollectionOfISBNs ( "9781449331818" ) );
        AddBooksByISBNPage addBooksByISBNPageRequestBody = new AddBooksByISBNPage ( userID , collectionOfIsbns );

        Response response = RequestEndpoints.postData ( addBooksByISBNPageRequestBody , ConfigLoader.getProperty ( "addBook" ) , token )
                .then ( )
                .statusCode ( HttpStatus.SC_CREATED )
                .body ( notNullValue ( ) ).extract ( ).response ( );

        ListBooks booksResponse = response.as ( ListBooks.class );
        Isbn = booksResponse.getBooks ( ).getFirst ( ).getIsbn ( );
        Isbn2 = booksResponse.getBooks ( ).getLast ( ).getIsbn ( );

    }


    @Test ( priority = 6, dependsOnMethods = { "registerUserTest" , "generateTokenTest_Success" , "addListOfBooksTest" }, description = "update books list of user by userId and ISBN: Validate Response Status, Headers, and Response Body" )
    public void updateBookListByISBN ( ) {
        UpdateBooksPage updateBooksPageReq = new UpdateBooksPage ( userID , "9781449337711" );
        Response response = RequestEndpoints.updateBookDetails ( updateBooksPageReq , ConfigLoader.getProperty ( "addBook" ) , token , Isbn )
                .then ( )
                .statusCode ( HttpStatus.SC_OK )
                .body ( notNullValue ( ) ).extract ( ).response ( );

        UpdateBooksPageResponse updateBooksPageResponse = response.as ( UpdateBooksPageResponse.class );
        System.out.println ( updateBooksPageResponse.getUsername ( ) );

        Assert.assertEquals ( username , updateBooksPageResponse.getUsername ( ) );
        Assert.assertEquals ( userID , updateBooksPageResponse.getUserId ( ) );
        Assert.assertNotNull ( updateBooksPageResponse.getBooks ( ) );

    }

    @Test ( priority = 7, dependsOnMethods = { "registerUserTest" , "generateTokenTest_Success" , "addListOfBooksTest" }, description = "Delete book from collection by ISBN: Validate Response Status, Headers, and Response Body" )
    public void deleteBookListByISBN ( ) {
        UpdateBooksPage updateBooksPage = new UpdateBooksPage ( userID , Isbn2 );
        Response response = RequestEndpoints.deleteBook ( updateBooksPage , ConfigLoader.getProperty ( "deleteBook" ) , token )
                .then ( )
                .statusCode ( 204 )
                .body ( nullValue ( ) ).extract ( ).response ( );
        assertThat("Content-Type header should be null", response.getHeader("Content-Type"), is(nullValue()));

    }

}
