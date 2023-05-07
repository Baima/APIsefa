API de Pagamentos:

Requisitos
1- A API deve ser capaz de receber um pagamento.
	Um pagamento possui os seguintes campos:
	Código do Débito sendo pago (valor inteiro)
	CPF ou CNPJ do Pagador
	Método de pagamento (boleto, pix, cartao_credito ou cartao_debito)
	Número do cartão
	(Este campo só será enviado se o método de pagamento for cartao_credito ou cartao_debito.)
	Valor do pagamento
2-A API deve ser capaz de atualizar o status de um pagamento.
	A atualização do status de um pagamento sempre irá conter o ID do Pagamento e o novo status.
	Quando o pagamento está Pendente de Processamento, ele pode ser alterado para Processado com Sucesso ou Processado com Falha.
	Quando o pagamento está Processado com Sucesso, ele não pode ter seu status alterado.
	Quando o pagamento está Processado com Falha, ele só pode ter seu status alterado para Pendente de Processamento.
3-A API deve ser capaz de listar todos os pagamentos recebidos e oferecer filtros de busca para o cliente.
	Os filtros de busca devem ser:
		a)Por código do débito
		b)Por CPF/CNPJ do pagador
		c)Por status do pagamento
4-A API deve ser capaz de deletar um pagamento, desde que este ainda esteja com status Pendente de Processamento.

Endpoints:

GET:

"/getTodos" : retorna lista de todos os pagamentos cadastrados

"/getPagamentoPorId/{id}" : retorna pagamento por id

"/getPagamentoPorDocumento/{doc}" : retorna lista de pagamentos por número do cpf/cnpj

"/getPagamentoPorStatus/{status}" : retorna lista de pagamentos por status de processamento (Pendente,Sucesso,Falha)

"/getPagamentoPorCodigo/{cod}" : retorna pagamento por código de cadastro

POST:

"/adicionarPagamento" : adiciona pagamento. Campos:
	"valor" (float),
    "codigo": (int),
    "cpfCnpj": (String),
    "metodoPagamento": (String) -> "cartao_credito","cartao_debito","pix","boleto"
    "numeroCartao": (int) *somente se metodoPagamento == "cartao_credito" ou "cartao_debito"

"/atualizaPagamentoPorId/{id}" : atualiza status de um pagamento. Campos:
	"statusProcessamento": -> "Pendente","Falha","Sucesso"

DELETE:

"/removePagamentoPorId/{id}" : remove um pagamento por id *somente se o status == "Pendente"*