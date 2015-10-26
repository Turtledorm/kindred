#!/bin/bash
# Script que instala e configura pacotes necessários para rodar o Pygame

set -e  # Finaliza script no primeiro caso de erro

# --------------------------------------------------------------------
# VARIÁVEIS GLOBAIS

# Diretório do projeto
DIR=`cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd`

# Diretório contendo arquivos para relacionar Java com Python
JYDIR="$DIR/jython"

# URL para baixar Jython
JYTHON_URL="search.maven.org/remotecontent?filepath=org/python/jython-standalone/2.7.0/jython-standalone-2.7.0.jar"

# Arquivo com as dependências a serem instaladas pelo Pip
REQ="$DIR/doc/requisitos/pip.txt"

# Arquivo contendo todos os arquivos instalados pelo Pygame
PYGAME_FILES="$DIR/doc/requisitos/pygame.txt"

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
    case $arg in
        -h)
            uso;;
        -r)
            echo -e $CYAN"Removendo Pygame e dependências..."$NORMAL

            # Remove pacotes de Python ligados ao Pygame
            while read -r linha; do
                # Verifica se a dependência está instalada antes de removê-la
                if pip3 freeze | grep -q $linha; then
                    pip3 uninstall -vy $linha
                fi
            done < $REQ

            # Remove arquivos do Pygame em si
            rm -rf pygame/
            if [[ -f $PYGAME_FILES ]]; then
                cat $PYGAME_FILES | xargs rm -rvf
                rm -f $PYGAME_FILES*
            fi

            # Remove Jython e arquivos de compatibilidade Java-Python
            rm -rvf $JYDIR/jython.jar $JYDIR/pyj2d/

            echo -e $CYAN"Desinstalação feita com sucesso! :)"$NORMAL
            exit 0
    esac
done

# -- Instalação -- #

# Pacotes de Python locais são, geralmente, instalados em '~/.local/'
echo -e $CYAN"Instalando requisitos contidos em \"$REQ\"..."$NORMAL

while read -r linha; do
    # Verifica se cada dependência já está instalada
    if ! pip3 freeze | grep -q $linha; then
        pip3 install --user $linha
    fi
done < $REQ

# Verifica se Pygame já está instalado
if ! pip3 freeze | grep -q "pygame"; then
    echo -e $CYAN"Instalando Pygame..."$NORMAL
    rm -rf pygame/
    hg clone https://bitbucket.org/pygame/pygame

    # Grava arquivos instalados do Pygame em si em $PYGAME_FILES.
    # O uso de 'echo' pula automaticamente uma pergunta [y/n].
    echo | python3 pygame/setup.py install --user --record $PYGAME_FILES.temp
    rm -rf pygame/

    # Generaliza os arquivos onde o Pygame está instalado para diretórios
    touch $PYGAME_FILES
    while read -r linha; do
        nome=${linha%pygame/*}  # Nome recebe path antes de 'pygame/'
        if ! grep -Fxq ${nome}pygame/ $PYGAME_FILES; then
            echo ${nome}pygame/ >> $PYGAME_FILES
        fi
        echo $linha >> $PYGAME_FILES
    done < $PYGAME_FILES.temp
    rm -f $PYGAME_FILES.temp
else
    echo -e $CYAN"Pygame já está instalado!"$NORMAL
fi

# Baixa o Jython
echo -e $CYAN"Baixando Jython..."$NORMAL
wget $JYTHON_URL -O $JYDIR/jython.jar

# Verifica se pacotes de compatibilidade Python-Java já foram configurados
if [[ ! -d $JYDIR/pyj2d ]]; then
    echo -e $CYAN"Configurando pacotes para compatibilidade de Java com Python..."$NORMAL
    unzip $JYDIR/PyJ2D*.zip -d $JYDIR
else
    echo -e $CYAN"Compatibilidade de Java com Python já foi feita!"$NORMAL
fi

echo -e $CYAN"Pacotes instalados com sucesso! :)"$NORMAL
