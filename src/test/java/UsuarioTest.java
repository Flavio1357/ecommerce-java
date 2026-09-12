import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import org.junit.jupiter.api.Test;

import model.Cliente;
import model.Funcionario;
import model.Usuario;

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

}