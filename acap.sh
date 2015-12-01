#!/bin/bash
# Cria um .tar.gz com os arquivos a serem enviados ao Paca
# (Fase 3 - Kindred)

set -e  # Abandona o script no primeiro erro encontrado

KINDRED_TAR="fase3.tar.gz"   # Nome do arquivo .tar.gz
KINDRED_DIR="kindred-fase3"  # Nome do diretório a ser compactado

SRCDIR="src"     # Diretório contendo código fonte
TESTDIR="test"   # Diretório contendo jars do JUnit e afins
REPDIR="report"  # Diretório contendo relatórios
DOCDIR="doc"     # Diretório contendo Javadocs

# -------------------------------------------------------------
# MAIN

# Verifica argumentos adicionais
for arg in "$@"; do
    case $arg in
    -c)
        rm -rvf $DOCDIR $KINDRED_DIR $KINDRED_TAR
        echo "Arquivos da compactação removidos com sucesso!"
        exit 0;;
    esac
done

# Inicialização
ant doc
mkdir -p $KINDRED_DIR

# Copia arquivos/diretórios
cp -rf build.xml run.sh $KINDRED_DIR
cp -rf $SRCDIR $TESTDIR $DOCDIR $KINDRED_DIR
cp -rf $REPDIR/fase3/main.pdf $KINDRED_DIR
mv $KINDRED_DIR/main.pdf $KINDRED_DIR/relatório.pdf

# Compactação
tar -cvf $KINDRED_TAR $KINDRED_DIR
rm -rf $KINDRED_DIR
echo -e "Arquivo \e[1;31m$KINDRED_TAR\e[0m criado com sucesso!"
