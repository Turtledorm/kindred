#!/bin/bash
# Script para compilar e executar o jogo Kindred

# --------------------------------------------------------------------
# VARIÁVEIS GLOBAIS

# Diretório do projeto
DIR=`cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd`

# Diretórios contendos código fonte do jogo
SRCDIR="$DIR/src"
GAMEDIR="kindred"

# Cores/efeitos para mensagens no terminal
NORMAL="\e[0m"
BOLD="\e[1m"

# --------------------------------------------------------------------
# Mostra como usar o script

function uso {
    echo -e $BOLD"USO"$NORMAL
    echo -e "\t./`basename $0` ["$BOLD"-h"$NORMAL"]\n"

    echo -e $BOLD"DESCRIÇÃO"$NORMAL
    echo -e "\tCompila e executa o jogo Kindred, localizado em '$SRCDIR'.\n"

    echo -e $BOLD"OPÇÕES"$NORMAL
    echo -e "\tSepare cada opção com um espaço.\n"
    echo -e "\t"$BOLD"-h"$NORMAL"\tMostra como usar o script, além de abandoná-lo.\n"

    exit
}

# --------------------------------------------------------------------
# MAIN 

# Argumentos por linha de comando
for arg in "$@"; do
    case $arg in
        -h)
            uso;;
    esac
done

cd $SRCDIR
javac $GAMEDIR/Main.java
java $GAMEDIR/Main
