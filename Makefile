.PHONY: all release clean
all: release
release: clean
	lein uberjar

clean:
	rm -rf resources/public/assets/js
	lein clean
