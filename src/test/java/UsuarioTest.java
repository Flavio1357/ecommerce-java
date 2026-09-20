import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import exception.ClienteNaoEncontradoException;
import exception.FuncionarioNaoEncontradoException;
import model.Cliente;
import model.Funcionario;
import model.Usuario;
import service.ClienteService;
import service.FuncionarioService;

public class UsuarioTest {

	@Test
	public void deveCriarClienteCorretamente() {

		Cliente cliente = new Cliente(
			"Rua A",
			1,
			"Flavio",
			"flavio@email.com",
			"123456",
			"12345678900"
		);

		assertEquals(1, cliente.getId());
		assertEquals("Flavio", cliente.getNome());
		assertEquals("flavio@email.com", cliente.getEmail());
		assertEquals("12345678900", cliente.getCpf());
		assertEquals("Rua A", cliente.getEndereco());
	}

	@Test
	public void deveCriarFuncionarioCorretamente() {

		Funcionario funcionario = new Funcionario(
			"Gerente",
			2,
			"Joao",
			"joao@email.com",
			"123456",
			"98765432100"
		);

		assertEquals(2, funcionario.getId());
		assertEquals("Joao", funcionario.getNome());
		assertEquals("joao@email.com", funcionario.getEmail());
		assertEquals("98765432100", funcionario.getCpf());
		assertEquals("Gerente", funcionario.getCargo());
	}

	@Test
	public void deveDemonstrarHeranca() {

		Cliente cliente = new Cliente(
			"Rua A",
			1,
			"Flavio",
			"flavio@email.com",
			"123456",
			"12345678900"
		);

		Funcionario funcionario = new Funcionario(
			"Gerente",
			2,
			"Joao",
			"joao@email.com",
			"123456",
			"98765432100"
		);

		assertInstanceOf(Usuario.class, cliente);
		assertInstanceOf(Usuario.class, funcionario);
	}

	@Test
	public void deveDemonstrarPolimorfismo() {

		Usuario cliente = new Cliente(
			"Rua A",
			1,
			"Flavio",
			"flavio@email.com",
			"123456",
			"12345678900"
		);

		Usuario funcionario = new Funcionario(
			"Gerente",
			2,
			"Joao",
			"joao@email.com",
			"123456",
			"98765432100"
		);

		cliente.exibirDados();
		funcionario.exibirDados();
	}

	@Test
	public void deveAutenticarCliente() {

		Cliente cliente = new Cliente(
			"Rua A",
			1,
			"Flavio",
			"flavio@email.com",
			"123456",
			"12345678900"
		);

		assertEquals(true, cliente.autenticar("123456"));
		assertEquals(false, cliente.autenticar("999999"));
	}

	@Test
	public void deveAutenticarFuncionario() {

		Funcionario funcionario = new Funcionario(
			"Gerente",
			2,
			"Joao",
			"joao@email.com",
			"123456",
			"98765432100"
		);

		assertEquals(true, funcionario.autenticar("123456"));
		assertEquals(false, funcionario.autenticar("999999"));
	}

	@Test
	public void deveBuscarClientePorId() {

    	Cliente cliente1 = new Cliente(
        	"Rua A",
        	1,
        	"Flavio",
        	"flavio@email.com",
        	"123",
        	"11111111111"
    	);

    	Cliente cliente2 = new Cliente(
        	"Rua B",
        	2,
        	"Joao",
        	"joao@email.com",
        	"456",
        	"22222222222"
    	);

    	List<Cliente> clientes = new ArrayList<>();
    	clientes.add(cliente1);
    	clientes.add(cliente2);

    	ClienteService service = new ClienteService();

    	Cliente resultado = service.buscarPorId(clientes, 2);

    	assertEquals("Joao", resultado.getNome());
	}

	@Test
	public void deveLancarExcecaoQuandoClienteNaoForEncontrado() {

    	Cliente cliente = new Cliente(
        	"Rua A",
        	1,
        	"Flavio",
        	"flavio@email.com",
        	"123",
        	"11111111111"
    	);

    	List<Cliente> clientes = new ArrayList<>();
    	clientes.add(cliente);

    	ClienteService service = new ClienteService();

    	assertThrows(
        	ClienteNaoEncontradoException.class,
        	() -> service.buscarPorId(clientes, 99)
    	);
	}

	@Test
	public void deveAutenticarClientePeloService() {

    	Cliente cliente = new Cliente(
        	"Rua A",
        	1,
        	"Flavio",
        	"flavio@email.com",
        	"123",
        	"11111111111"
    	);

    	ClienteService service = new ClienteService();

    	boolean autenticado = service.autenticar(cliente, "123");

    	assertTrue(autenticado);
	}

	@Test
	public void deveRecusarSenhaIncorretaDoCliente() {

    	Cliente cliente = new Cliente(
        	"Rua A",
        	1,
        	"Flavio",
        	"flavio@email.com",
        	"123",
        	"11111111111"
    	);

    	ClienteService service = new ClienteService();

    	boolean autenticado = service.autenticar(cliente, "999");

    	assertFalse(autenticado);
	}

	@Test
	public void deveBuscarFuncionarioPorId() {

    	Funcionario funcionario1 = new Funcionario(
        	"Gerente",
        	1,
        	"Joao",
        	"joao@email.com",
        	"123",
        	"11111111111"
    	);

    	Funcionario funcionario2 = new Funcionario(
        	"Analista",
        	2,
        	"Maria",
        	"maria@email.com",
        	"456",
        	"22222222222"
    	);

    	List<Funcionario> funcionarios = new ArrayList<>();
    	funcionarios.add(funcionario1);
    	funcionarios.add(funcionario2);

    	FuncionarioService service = new FuncionarioService();

    	Funcionario resultado = service.buscarPorId(funcionarios, 2);

    	assertEquals("Maria", resultado.getNome());
	}

	@Test
	public void deveLancarExcecaoQuandoFuncionarioNaoForEncontrado() {

    	Funcionario funcionario = new Funcionario(
        	"Gerente",
        	1,
        	"Joao",
        	"joao@email.com",
        	"123",
        	"11111111111"
    	);

    	List<Funcionario> funcionarios = new ArrayList<>();
    	funcionarios.add(funcionario);

    	FuncionarioService service = new FuncionarioService();

    	assertThrows(
        	FuncionarioNaoEncontradoException.class,
        	() -> service.buscarPorId(funcionarios, 99)
    	);
	}

	@Test
	public void deveAutenticarFuncionarioPeloService() {

    	Funcionario funcionario = new Funcionario(
        	"Gerente",
        	1,
        	"Joao",
        	"joao@email.com",
        	"123",
        	"11111111111"
    	);

    	FuncionarioService service = new FuncionarioService();

    	boolean autenticado = service.autenticar(funcionario, "123");

    	assertTrue(autenticado);
	}

	@Test
	public void deveRecusarSenhaIncorretaDoFuncionario() {

    	Funcionario funcionario = new Funcionario(
        	"Gerente",
        	1,
        	"Joao",
        	"joao@email.com",
        	"123",
        	"11111111111"
    	);

    	FuncionarioService service = new FuncionarioService();

    	boolean autenticado = service.autenticar(funcionario, "999");

    	assertFalse(autenticado);
	}

}