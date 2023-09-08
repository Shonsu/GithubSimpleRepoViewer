package pl.shonsu.githubsimplerepoviewer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GithubViewerExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(GithubNotFoundException.class)
    public ErrorMessage handleUserNotFoundException(GithubNotFoundException ex) {
        return new ErrorMessage(HttpStatus.NOT_FOUND.toString(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(XmlNotAcceptableRepresentationException.class)
    public ResponseEntity<ErrorMessage> handleUserNotFoundException(XmlNotAcceptableRepresentationException ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(HttpStatus.NOT_ACCEPTABLE.toString(), ex.getMessage()));
    }

}
