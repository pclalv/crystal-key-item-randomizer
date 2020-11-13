GIT_COMMIT=$(shell git rev-parse head)

.PHONY: all release clean
all: release
release: clean
	lein uberjar
	mv ./target/uberjar/crystal-key-item-randomizer.jar ./target/uberjar/crystal-key-item-randomizer-$(GIT_COMMIT).jar

clean:
	rm -rf resources/public/assets/js
	lein clean
