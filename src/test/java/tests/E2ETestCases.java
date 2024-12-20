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

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class E2ETestCases extends BaseTest {

    @Test ( description = "User Registration API with authentication: Validate Response status, Headers, and Response Body" )
    public void registerUserTest ( ) {
        Response response = RequestEndpoints.postData ( userDetails , ConfigLoader.getProperty ( "registerUser" ) )
                .then ( )
                .statusCode ( HttpStatus.SC_CREATED )
                .body ( matchesJsonSchemaInClasspath ( "schemas/RegisterJsonSchema.json" ) ).extract ( ).response ( );
        RegisterResponse registerResponse = response.as ( RegisterResponse.class );
        userID = registerResponse.getUserID ( );
        logger.info ( "User Registration test passed successfully" );
    }

    @Test ( description = "check for existing user registration" )
    public void userAlreadyExistsTest ( ) {
        RequestEndpoints.postData ( userDetails , ConfigLoader.getProperty ( "registerUser" ) )
                .then ( )
                .statusCode ( HttpStatus.SC_NOT_ACCEPTABLE )
                .body ( "code" , equalTo ( "1204" ) )
                .body ( "message" , equalTo ( "User exists!" ) );
        logger.info ( "Already existing user test passed successfully" );
    }

    @Test (dependsOnMethods = {"registerUserTest"}, description = "Generate Token: Success- Validate Response Status, Headers, and Response Body" )
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
        logger.info ( "Generate Token test passed successfully" );
    }

    @Test ( description = "Get All Books: Validate Response Status, Headers, and Response Body" )
    public void getAllBooksTest ( ) {
        Response response = RequestEndpoints.getData ( ConfigLoader.getProperty ( "getBooks" ) )
                .then ( )
                .statusCode ( HttpStatus.SC_OK )
                .body ( "books" , notNullValue ( ) )
                .body ( matchesJsonSchemaInClasspath ( "schemas/Book.json" ) ).extract ( ).response ( );
            logger.info ( "Get all Books test passed successfully" );

    }

    @Test (description = "Get Book by ISBN: Validate Response Status, Headers, and Response Body" )
    public void getBookByISBNTest ( ) {
        RequestEndpoints.getData ( ConfigLoader.getProperty ( "getBooks" ) , "9781449325862" )
                .then ( )
                .statusCode ( HttpStatus.SC_OK );
        logger.info ( "Get Book By ISBN test passed successfully" );
    }

    @Test ( description = "Add list of Books into user's book collection: Validate Response Status, Headers, and Response Body", dependsOnMethods = { "registerUserTest" , "generateTokenTest_Success" } )
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
        logger.info ( "Add books to user test passed successfully" );

    }


    @Test ( dependsOnMethods = { "registerUserTest" , "generateTokenTest_Success" , "addListOfBooksTest" }, description = "update books list of user by userId and ISBN: Validate Response Status, Headers, and Response Body" )
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
        logger.info ( "Update book test passed successfully" );

    }

    @Test ( dependsOnMethods = { "registerUserTest" , "generateTokenTest_Success" , "addListOfBooksTest", "updateBookListByISBN"}, description = "Delete book from collection by ISBN: Validate Response Status, Headers, and Response Body" )
    public void deleteBookListByISBN ( ) {
        UpdateBooksPage updateBooksPage = new UpdateBooksPage ( userID , Isbn2 );
        RequestEndpoints.deleteBook ( updateBooksPage , ConfigLoader.getProperty ( "deleteBook" ) , token )
                .then ( )
                .statusCode ( 204 )
                .contentType (equalTo ( "" ));
    }

}
