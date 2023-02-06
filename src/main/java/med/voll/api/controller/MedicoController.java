package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.medico.DadosAtualizacaoMedico;
import med.voll.api.medico.DadosCadastroMedico;
import med.voll.api.medico.DadosListagemMedicos;
import med.voll.api.medico.Medico;
import med.voll.api.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroMedico dados) {
        repository.save(new Medico(dados));
    }

    @GetMapping
    //@PageableDefault() para alterar os padrões. (size, page, sort)
    public Page<DadosListagemMedicos> listar(@PageableDefault (size = 10, sort = {"nome"}) Pageable paginacao) {
        //conversão de Medicos para DadosListagemMedicos, usando .stream() e fazer um mapeamento com .map()
        //DadosListagemMedicos::new chama o construtor de DadosListagemMedicos
        //.toList() para converter em uma lista

        //para retornar todos os dados do banco, mesmo estando inativo
        //return repository.findAll(paginacao).map(DadosListagemMedicos::new);
        return repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedicos::new);
    }

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados) {
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);
    }

    //  entre {} parametros dinamicos
    @DeleteMapping("/{id}")
    @Transactional
    //@PathVariable para informar que o id do parametro é o mesmo id da url
    public void excluir(@PathVariable Long id) {
        // método para excluir direto no banco de dados.
        //repository.deleteById(id);
        var medico = repository.getReferenceById(id);
        medico.deixarInativo();
    }
}
