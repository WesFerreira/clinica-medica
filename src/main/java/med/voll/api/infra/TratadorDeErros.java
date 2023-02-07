package med.voll.api.infra;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileFilter;

// Anotação para indicar que é uma classe para tratar erros na API
@RestControllerAdvice
public class TratadorDeErros {

    //passar a classe exception especifica
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarErro404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    //recebendo a exception lançada declarando como parametro na assinatura do método para pegar os erros
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();

        return ResponseEntity.badRequest().body(erros.stream().map(DadosErrosValidacao::new).toList());
    }

    //record direto na classe, porq vamos usar esse record apenas nessa classe.
    private record DadosErrosValidacao(String nome, String mensagem) {
        public DadosErrosValidacao (FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }

}
