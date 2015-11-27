#!/bin/bash
# Script para compilar e executar o jogo Kindred

# --------------------------------------------------------------------
# VARIÁVEIS GLOBAIS

# Diretório do projeto
DIR=`cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd`

# Cores/efeitos para mensagens no terminal
NORMAL="\e[0m"
BOLD="\e[1m"

# --------------------------------------------------------------------
# Mostra como usar o script

function uso {
    echo -e $BOLD"USO"$NORMAL

    ARGS="["$BOLD"-c"$NORMAL" [IP]] ["$BOLD"-s"$NORMAL"]"
    ARGS=$ARGS" ["$BOLD"-C"$NORMAL" [IP]] ["$BOLD"-S"$NORMAL"]"
    ARGS=$ARGS" ["$BOLD"-h"$NORMAL"]"

    echo -e "\t./`basename $0` $ARGS\n"

    echo -e $BOLD"DESCRIÇÃO"$NORMAL
    echo -e "\tExecuta o jogo Kindred de acordo com o argumento dado.\n"

    echo -e $BOLD"OPÇÕES"$NORMAL
    echo -e "\tSepare cada opção com um espaço.\n"

    echo -e "\t"$BOLD"-c"$NORMAL"\tExecuta o jogo Kindred como cliente (usando bin/)."
    echo -e "\t\tOpcionalmente, pode receber o IP do servidor a qual se conecta.\n"

    echo -e "\t"$BOLD"-s"$NORMAL"\tExecuta o jogo Kindred como servidor (usando bin/).\n"
    
    echo -e "\t"$BOLD"-C"$NORMAL"\tExecuta o jogo Kindred como cliente (usando release/)."
    echo -e "\t\tOpcionalmente, pode receber o IP do servidor a qual se conecta.\n"

    echo -e "\t"$BOLD"-S"$NORMAL"\tExecuta o jogo Kindred como servidor (usando release/).\n"

    echo -e "\t"$BOLD"-h"$NORMAL"\tMostra como usar o script, além de abandoná-lo."

    exit 1
}

# --------------------------------------------------------------------
# MAIN 

if (( $# == 0 )); then uso; fi

# Argumentos por linha de comando
case $1 in
    -h)
        uso;;
    -s)
        java -cp bin/ kindred.server.Server;;
    -c)
        java -cp bin/ kindred.client.network.Client $2;;
    -S)
        java -jar release/jar/KindredServer.jar;;
    -C)
        java -jar release/jar/KindredClient.jar $2;;
    *)
        echo "Argumento '$arg' não reconhecido!"
        uso;;
esac
