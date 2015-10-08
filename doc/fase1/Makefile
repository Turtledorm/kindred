SHELL    =bash
MP       =$(wildcard *.mp)
DIAGRAMS =$(MP:.mp=.1)
TARGET   =main.pdf

all: $(TARGET)

$(TARGET): $(DIAGRAMS) .FORCE
	@[ -d aux/ ] || mkdir aux ||:
	@cd aux && mv * .. &>/dev/null ||:
	latexmk -interaction=nonstopmode -pdf $(TARGET:.pdf=)
	@mv *.aux *.fls *.log *.fdb_latexmk *.toc aux &>/dev/null ||:

$(DIAGRAMS): %.1 : %.mp
	echo teste
	mpost -interaction=nonstopmode $< 

.FORCE:


.PHONY:
clean:
	latexmk -c
	rm -rf $(TARGET) $(DIAGRAMS)

.PHONY:
view:
	(evince $(TARGET) 2>/dev/null || atril $(TARGET) 2>/dev/null) &
