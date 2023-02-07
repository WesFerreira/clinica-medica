package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.medico.*;
import med.voll.api.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder) {
        var medico = new Medico(dados);
        repository.save(medico);

        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));

    }

    @GetMapping
    //@PageableDefault() para alterar os padrões. (size, page, sort)
    public ResponseEntity<Page<DadosListagemMedicos>> listar(@PageableDefault (size = 10, sort = {"nome"}) Pageable paginacao) {
        //conversão de Medicos para DadosListagemMedicos, usando .stream() e fazer um mapeamento com .map()
        //DadosListagemMedicos::new chama o construtor de DadosListagemMedicos
        //.toList() para converter em uma lista

        //para retornar todos os dados do banco, mesmo estando inativo
        //return repository.findAll(paginacao).map(DadosListagemMedicos::new);
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedicos::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados) {
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

    //  entre {} parametros dinamicos
    @DeleteMapping("/{id}")
    @Transactional
    //@PathVariable para informar que o id do parametro é o mesmo id da url
    public ResponseEntity excluir(@PathVariable Long id) {
        // método para excluir direto no banco de dados.
        //repository.deleteById(id);
        var medico = repository.getReferenceById(id);
        medico.deixarInativo();
        //noContent() para retornar o 204 e .buil() porq o noContent() ele não devolve o ResponseEntity e o .build() sim
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        var medico = repository.getReferenceById(id);
        return  ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }
}
