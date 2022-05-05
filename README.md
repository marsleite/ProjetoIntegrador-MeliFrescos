# ProjetoIntegrador - Mercado Livre: Frescos (Requisito 06)


Este módulo do [Projeto Integrador](https://github.com/juliocesargama/ProjetoIntegrador-MeliFrescos) ao que se destina a simular uma nova modalidade de armazenamento, transporte e comercialização de produtos frescos, congelados e refrigerados do Mercado Livre através de uma API REST em Java com Spring Boot e suas respectivas dependências, na qual foi adicionada uma nova funcionalidade(Requisito 06, no formato individual) a fim de disponibilizar cupons de desconto ao compradores do marketplace, dadas as condições de certos produtos comercializados na plataforma.

Para mais detalhes, consulte a documentação detalhada do UserStory do requisito disponível neste [link](https://github.com/marsleite/ProjetoIntegrador-MeliFrescos/blob/development/Requisito%206%20-%20Marcelo.docx.pdf).

### Características e Tecnologias:
- Java 11;
- Spring Security e Token JWT;
- Spring Validations;
- Spring Data JPA;
- Banco de Dados relacional Postgres (local).

### Instruções para a instalação:

Para acesso local do banco de dados, é necessário a inserção da variável de ambiente abaixo na IDE:

```sh
HOST=jdbc:postgresql://localhost:5432/PIDB;USERNAME=(seu_nome_de_usuário);PASSWORD=(sua_senha_definida);EMAILNAME={EMAIL};EMAILPASS={SENHA_APP_GOOGLE_EMAIL}
```

### Collection com os End-points no Postman:

Encontra-se dentro do projeto, no diretório abaixo:

```sh
src/main/resources/PostmanCollection.json
```

### Diagrama de Classe (Requisito 06 em vermelho)
![REQ06 drawio](https://github.com/marsleite/ProjetoIntegrador-MeliFrescos/blob/development/DRE.png)


### Documentação, Referencial utilizados e Cronologia dos requisitos:

[Enunciado Base](https://drive.google.com/file/d/1bBOM49bxqRR7apxP3sgV7_LRiTq9xQD2/view)

[Requisito 1](https://drive.google.com/file/d/1rbT3upYAwN-CrOVtze0M2Fq7Cobuj7FD/view) (Início em: 22/04/22, Término em: 27/04/22)

[Requisito 2](https://drive.google.com/file/d/1M66St3F6TwWJ6WG_s1in75_bMyeKb8PM/view) (Início em: 26/04/22, Término em: 02/05/22)

[Requisito 3](https://drive.google.com/file/d/1GnTl6sHhdvyKjR0oz0nXlyvzH-oW_2Jv/view) (Início em: 28/04/22, Término em: 29/04/22)

[Requisito 4](https://drive.google.com/file/d/1kNZLztafr2tXuDU24W9xwUu09va2kMP0/view) (Início em: 29/04/22, Término em: 02/05/22)

[Requisito 5](https://drive.google.com/file/d/1yiEzdwI87K7AO9bgPffHbb0DPjVKM-oP/view) (Início em: 29/04/22, Término em: 03/05/22)

[Requisito 6](https://drive.google.com/file/d/1zlRtIPjK4r0WdrzFs7LIVA_8Q5HyDgXz/view) (Início em: 03/05/22, Término em: 06/05/22)
