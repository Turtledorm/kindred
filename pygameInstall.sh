#!/bin/bash
# Script que instala e configura pacotes necessários para rodar o Pygame

set -e  # Finaliza script no primeiro caso de erro

# --------------------------------------------------------------------
# Variáveis globais

# Diretório do projeto
DIR=`cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd`

# Diretório contendo arquivos relacionados a jython
JYTHON="$DIR/jython"

# Arquivo com as dependências
REQ="$DIR/doc/requisitos/pip.txt"

# Cores/efeitos para mensagens no terminal
NORMAL="\e[0m"
BOLD="\e[1m"
CYAN="\e[36m"

# --------------------------------------------------------------------
# Mostra como usar o script

function uso {
    echo -e $BOLD"USO"$NORMAL
    echo -e "\t./`basename $0` ["$BOLD"-h"$NORMAL"] ["$BOLD"-r"$NORMAL"]\n"

    echo -e $BOLD"DESCRIÇÃO"$NORMAL
    echo -e "\tInstala as dependências necessárias para rodar o Pygame."
    echo -e "\tAs dependências estão no arquivo '$REQ'.\n"

    echo -e $BOLD"OPÇÕES"$NORMAL
    echo -e "\tSepare cada opção com um espaço.\n"
    echo -e "\t"$BOLD"-h"$NORMAL"\tMostra como usar o script, além de abandoná-lo.\n"
    echo -e "\t"$BOLD"-r"$NORMAL"\tRemove os pacotes instalados por este script," \
            "além de abandoná-lo.\n"

    exit
}

# --------------------------------------------------------------------
# MAIN 

# Argumentos por linha de comando
for arg in "$@"; do
    if [ "$arg" == "-h" ]; then uso; fi
    if [ "$arg" == "-r" ]; then
        echo -e $CYAN"Desfazendo configurações do Pygame..."$NORMAL
        pip3 uninstall -yr $REQ
        rm -rf $JYTHON/pyj2d
        echo -e $CYAN"Pygame desinstalado! :)"$NORMAL
        exit 0
    fi
done

if [ -f "$REQ" ]; then
    echo -e $CYAN"Instalando requisitos contidos em \"$REQ\"..."$NORMAL
    # Precisa dar um Enter imperceptível no pip3 abaixo, por isso o 'echo'
    echo | pip3 install --user -r $REQ --allow-external pygame --allow-unverified pygame

    echo -e $CYAN"Configurando pacotes para compatibilidade de Java com Python..."$NORMAL
    unzip $JYTHON/PyJ2D*.zip
fi

echo -e $CYAN"Pacotes instalados com sucesso! :)"$NORMAL
