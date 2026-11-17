# Sistema de Controle de Condomínio — POO

Projeto Java 17 com interface gráfica Swing, organizado por classes e sem bibliotecas externas.

## Como executar no VS Code
1. Instale um JDK 17 ou superior.
2. Instale no VS Code a extensão **Extension Pack for Java**.
3. Extraia a pasta `condominio-poo`.
4. Abra essa pasta no VS Code.
5. Abra `src/main/java/br/com/condominio/Main.java` e clique em **Run Java** (ou execute `br.com.condominio.Main`).

Também é possível compilar no terminal, dentro da pasta:
```bash
javac -encoding UTF-8 -d out src/main/java/br/com/condominio/*.java
java -cp out br.com.condominio.Main
```

## Funcionalidades
- Cadastro, edição, listagem e remoção de unidades: apartamento, casa e sala comercial.
- Cálculo de taxa condominial por metragem, fração ideal ou valor fixo comercial.
- Cadastro de moradores/responsáveis vinculados a unidades.
- Cadastro de áreas comuns reserváveis.
- Reserva com validação de conflito de data/horário e cobrança extra opcional.
- Geração de cobrança mensal somando taxa da unidade e reservas do mês.
- Relatórios de inadimplência (status de pagamento) e utilização de áreas comuns.

## Observação
Os dados ficam em memória: ao fechar o programa, os cadastros e cobranças são reiniciados. Para uma entrega escolar, isso deixa o projeto simples de executar; persistência em arquivo/banco pode ser adicionada depois.
