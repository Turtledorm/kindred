#!/bin/bash
# Script que acrescenta um virtualenv em um diretório já existente.

# Finaliza script no primeiro caso de erro
set -e

# --------------------------------------------------------------------
# Variáveis globais

# Diretório do projeto
DIR=`cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd`

# Arquivo com as dependências
REQ="$DIR/doc/requisitos/pip.txt"

# Cores para mensagens no terminal
CYAN="\e[36m"
WHITE="\e[0m"

# --------------------------------------------------------------------
# Mostra como usar o script.

function uso {
    echo -e "\e[1mUSO\e[0m"
    echo -e "\t./`basename $0` [\e[1m-h\e[0m] [\e[1m-r\e[0m]\n"

    echo -e "\e[1mDESCRIÇÃO\e[0m"
    echo -e "\tCria um virtualenv para Python 3 no diretório atual."
    echo -e "\tTambém instala as dependências encontradas em '$REQ'.\n"

    echo -e "\e[1mOPÇÕES\e[0m"
    echo -e "\tSepare cada opção com um espaço.\n"
    echo -e "\t\e[1m-h\e[0m\tMostra como usar o script, além de abandoná-lo.\n"
    echo -e "\t\e[1m-r\e[0m\tRemove os diretórios instalados por este script," \
            "além de abandoná-lo.\n"

    exit
}

# --------------------------------------------------------------------
# MAIN 

# Argumentos por linha de comando
for arg in "$@"; do
    if [ "$arg" == "-h" ]; then uso; fi
    if [ "$arg" == "-r" ]; then
        rm -rfv bin/ include/ lib/ pip-selfcheck.json
        echo "Diretórios do virtualenv foram removidos com sucesso!"
        exit 0
    fi
done

echo -e $CYAN"Criando o virtualenv..."$WHITE
virtualenv -p python3 $DIR

if [ -f "$REQ" ]; then
    source $DIR/bin/activate

    echo -e $CYAN"Instalando requisitos contidos em \"$REQ\"..."$WHITE
    pip3 install -r $REQ

    echo -e $CYAN"Instalando pygame..."$WHITE
    # Precisa dar um Enter imperceptível ao usar o 'hg' abaixo,
    # então faz-se echo de um '\n'.
    echo -e "\n" | pip3 install hg+http://bitbucket.org/pygame/pygame
    rm -f pip-selfcheck.json
fi

echo "Pronto! :)"
