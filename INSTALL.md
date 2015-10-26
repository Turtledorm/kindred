Instalação
==========

Este manual supõe que o usuário esteja usando Linux e que tenha acesso ao Terminal,
que será a ferramenta utilizada tanto na instalação quanto na desinstalação. Deve-se
estar conectado à Internet para baixar alguns pacotes.


1. Instale, em seu sistema, os pacotes especificados em [doc/requisitos/sistema.txt]
   (doc/requisitos/sistema.txt "Link para arquivo"). Este comando provavelmente requer
   que o usuário consiga rodar comandos de administrador. Se quiser instalar rapidamente
   todos os pacotes de uma vez, rode o seguinte comando no *root* deste projeto (usou-se
   **apt-get install** abaixo; troque pelo equivalente no seu sistema):

        $ cat doc/requisitos/sistema.txt | xargs sudo apt-get install -y

    **Importante:** Recomenda-se que o usuário guarde quais pacotes foram instalados
    nesta etapa se desejar removê-los depois, pois isso não é feito por este projeto.


2. Execute o script **pygameInstall.sh**, localizado no *root* deste projeto, para instalar
   o módulo [Pygame](https://en.wikipedia.org/wiki/Pygame "Mais informações sobre Pygame")
   e suas dependências.

        $ ./pygameInstall.sh

    Se desejar obter maiores instruções sobre o que esse script faz, execute-o com
    o argumento **-h**:

        $ ./pygameInstall.sh -h


Desinstalação
-------------

1. Para remover o módulo Pygame e suas dependências, execute o script **pygameInstall.sh**
   com o argumento **-r**:

        $ ./pygameInstall.sh -r
