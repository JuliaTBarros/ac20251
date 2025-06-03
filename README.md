# Sistema de Gerenciamento de Seguros

## Visão Geral

Este projeto é um sistema de gerenciamento de seguros baseado em Java. Ele lida com entidades como segurados (pessoas físicas e jurídicas), veículos, apólices de seguro e sinistros. O sistema oferece funcionalidades para criar, ler, atualizar e deletar (CRUD) essas entidades, juntamente com a lógica de negócios para criação de apólices e processamento de sinistros.

## Funcionalidades

* **Gerenciamento de Segurados**:
    * Gerencia segurados individuais (`SeguradoPessoa`) e segurados corporativos (`SeguradoEmpresa`).
    * Valida CPF/CNPJ, nome, endereço e outros dados relevantes.
* **Gerenciamento de Veículos**:
    * Armazena informações do veículo, incluindo placa, ano, proprietário e categoria.
    * Suporta diferentes categorias de veículos (`CategoriaVeiculo`) com preços associados por ano.
* **Gerenciamento de Apólices (`Apolice`)**:
    * Cria e gerencia apólices de seguro.
    * Calcula prêmios e franquias com base no valor do veículo, categoria, histórico do segurado e bônus.
    * Lida com a exclusão de apólices com verificações de sinistros existentes.
* **Gerenciamento de Sinistros (`Sinistro`)**:
    * Registra e gerencia sinistros associados a apólices.
    * Valida dados do sinistro, incluindo data, hora, veículo, apólice envolvida e valor do sinistro em relação aos limites da apólice.
    * Suporta diferentes tipos de sinistros (`TipoSinistro`): Colisão, Incêndio, Furto, Enchente, Depredação.
* **Persistência de Dados**:
    * Usa um padrão DAO genérico (`DAOGenerico`) para operações de acesso a dados.
    * DAOs específicos para cada entidade (por exemplo, `ApoliceDAO`, `VeiculoDAO`, `SeguradoPessoaDAO`, `SeguradoEmpresaDAO`, `SinistroDAO`).
* **Validação de Entrada**:
    * Validação abrangente para todas as entradas de dados críticos.
    * Tratamento de exceções personalizado para erros de validação (`ExcecaoValidacaoDados`).

## Estrutura de Pastas

O espaço de trabalho está organizado da seguinte forma:

* `src/br/edu/cs/poo/ac/seguro/`: Diretório principal do código-fonte.
    * `daos/`: Contém classes Data Access Object (DAO) para interagir com a camada de persistência de dados.
    * `entidades/`: Contém as classes de entidade principais (POJOs) que representam os objetos de domínio como `Apolice`, `Veiculo`, `Segurado`, `Sinistro`, etc.
    * `excecoes/`: Contém classes de exceção personalizadas.
    * `gui/`: Contém as classes da interface gráfica do usuário (Swing).
    * `mediators/`: Contém classes que orquestram ações entre DAOs e entidades, implementando a lógica de negócios.
    * `testes/`: Contém testes JUnit para vários componentes do sistema.
* `lib/`: Contém dependências do projeto, como `lombok.jar`.
* `.idea/`: Arquivos de configuração específicos do projeto IntelliJ IDEA.
* `.vscode/`: Configurações específicas do VS Code, incluindo configuração do projeto Java.
* `bin/`: Diretório de saída padrão para arquivos compilados (conforme `.vscode/settings.json`).

## Tecnologias Utilizadas

* **Java**: Linguagem de programação principal.
* **Swing**: Para a interface gráfica do usuário.
* **Lombok**: Usado para reduzir código boilerplate para POJOs (por exemplo, `@Getter`, `@Setter`, `@AllArgsConstructor`).
* **JUnit 5**: Para testes unitários.
* **PersistenciaObjetos.jar**: Uma biblioteca personalizada para persistência de objetos (detalhes inferidos de `PersistenciaObjetos.xml` e `DAOGenerico.java`).

## Começando

### Pré-requisitos

* Java Development Kit (JDK) instalado (Versão 17, conforme indicado em `misc.xml`).
* Um IDE como IntelliJ IDEA ou VS Code com suporte Java.

### Configuração

1.  Clone o repositório.
2.  Certifique-se de que a pasta `lib` com `lombok.jar` e `PersistenciaObjetos.jar` (se for um jar local, certifique-se de que está referenciado corretamente; o caminho `../PersistenciaObjetos.jar` em `PersistenciaObjetos.xml` sugere que pode estar fora desta pasta específica do projeto) esteja configurada corretamente no classpath do seu IDE.
    * Para o VS Code, o arquivo `.vscode/settings.json` referencia bibliotecas em `lib/**/*.jar` e um caminho específico para `lombok.jar`.
    * Para o IntelliJ, os arquivos XML em `.idea/libraries/` definem os caminhos das bibliotecas.
3.  O projeto usa processamento de anotações para o Lombok, que deve estar habilitado em seu IDE.

### Executando Testes

O projeto inclui um conjunto de testes JUnit no diretório `src/br/edu/cs/poo/ac/seguro/testes/`. Eles podem ser executados a partir do seu IDE para verificar a funcionalidade de diferentes módulos.

### Executando a Interface Gráfica

As telas da interface gráfica podem ser executadas individualmente através de seus métodos `main`.
* `TelaCrudSeguradoPessoa.java`: Para gerenciar segurados pessoa física.
* `TelaCrudSeguradoEmpresa.java`: Para gerenciar segurados pessoa jurídica.
* `TelaInclusaoApolice.java`: Para incluir novas apólices de seguro.
* `TelaInclusaoSinistro.java`: Para registrar novos sinistros.

## Como Usar

O sistema é estruturado em torno de classes "mediator" (`ApoliceMediator`, `SeguradoEmpresaMediator`, `SeguradoPessoaMediator`, `SinistroMediator`) que fornecem a interface primária para realizar operações, seja através da GUI ou programaticamente.

* Para gerenciar segurados, use `SeguradoPessoaMediator` para pessoas físicas e `SeguradoEmpresaMediator` para empresas. Estas funcionalidades são expostas nas telas `TelaCrudSeguradoPessoa` e `TelaCrudSeguradoEmpresa`.
* Para criar ou gerenciar apólices de seguro, use `ApoliceMediator`. A inclusão de apólices é feita através da `TelaInclusaoApolice`.
* Para registrar ou processar sinistros, use `SinistroMediator`. O registro de sinistros é feito através da `TelaInclusaoSinistro`.

Os Data Access Objects (DAOs) lidam com a interação direta com o mecanismo de armazenamento de dados, gerenciado pela classe `CadastroObjetos` da biblioteca `PersistenciaObjetos`.

## Interface Gráfica (GUI)

O sistema possui uma interface gráfica desenvolvida com Swing, permitindo a interação do usuário para as principais funcionalidades. As telas disponíveis são:

* **CRUD de Segurado Pessoa (`TelaCrudSeguradoPessoa.java`)**: Permite criar, buscar, alterar e excluir segurados do tipo pessoa física.
* **CRUD de Segurado Empresa (`TelaCrudSeguradoEmpresa.java`)**: Permite criar, buscar, alterar e excluir segurados do tipo pessoa jurídica.
* **Inclusão de Apólice (`TelaInclusaoApolice.java`)**: Formulário para registrar novas apólices de seguro, associando um veículo e um segurado.
* **Inclusão de Sinistro (`TelaInclusaoSinistro.java`)**: Formulário para registrar ocorrências de sinistros vinculados a uma apólice existente.

Cada uma dessas telas utiliza as classes mediadoras para realizar as operações de negócio e interagir com a camada de persistência de dados.
