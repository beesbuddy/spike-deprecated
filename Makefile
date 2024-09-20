clean:
	rm -rf target

run:
	clj -M:dev

repl:
	clj -M:dev:nrepl

test-watch:
	clj -M:test --watch

uberjar:
	clj -T:build all

format:
	clj -T:cljfmt fix
