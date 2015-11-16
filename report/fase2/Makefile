SHELL    =bash
TARGET   =main.pdf
PREAMBLE =preamble-vbm
TEX      =*.tex

all: $(TARGET)

$(TARGET): .FORCE
	@[ -d aux/ ] || mkdir aux ||:
	@cd aux && mv * .. &>/dev/null ||:
	latexmk -interaction=nonstopmode  -pdf $(TARGET:.pdf=)
	@mv *.aux *.fls *.log *.fdb_latexmk *.toc aux &>/dev/null ||:

.FORCE:


.PHONY:
clean:
	latexmk -c
	rm -rf $(TARGET)

.PHONY:
view:
	(evince $(TARGET) 2>/dev/null || atril $(TARGET) 2>/dev/null) &
