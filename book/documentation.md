# Documentation of Documentation

Documentation is managed via [gitbook](https://toolchain.gitbook.com/).

We write markdown documentation as we feel necessary. Document each directory using `README.md` files and if possible,
link them together via `SUMMARY.md` in the root folder. There may be multiple `README-*.md` files which can be linked
from each other. However, each of the files must be mentioned (linked to) in `SUMMARY.md`.

Document also the main implementation details, aspects, which required the change of implementation along with the
analysis/reasons for the new solution to avoid similar problems in the future.

## Conventions

If you refer to a file (or pathname), mark it up like `file.extension`. You will not encounter *Markdown* problems with
names like `__init__.py` which contain nested *Markdown* markup. Even if there is no special character in the name, stay
to the convention to keep the documentation uniform.

If you refer to a module (or project) name, mark it up like this: *Module*

## Creating the Documentation

To run the *gitbook server* from the root project directory use `make book-serve`. This `Makefile` goal just runs
gitbook in docker. To automatically reload as you type, use `serve.sh` from `book` directory. To stop serving, run
`make-kill`.

### Generating PDF

To generate the documentation in a `pdf` form run the following command from the root directory:

```bash
make book-pdf
```

## Running Natively

Install `node` and `npm` and just use `serve.sh`. Then navigate to [http://localhost:4003](http://localhost:4003/) to
see the document.

Although the documentation root is in `book`, the `serve.sh` script ensures the live reloading of the book even if the
files outside of the `book` directory change. To do so, it uses `inoticoming` file watcher for relevant files and
notifies gitbook to update, if a file has changed. Live reloading is an optional feature which does not necessarily work
correctly in all cases (graphs/UMLs update). In case it did not run correctly, kill the `serve.sh` script and run it
again.

## Plugins

*Katex* and *PlantUML*.

### Inline Math

$$\int_{-\infty}^\infty g(x) dx$$

Upon adding a new plugin, please run `rm -rf book/_book book/node_modules`.

## Versioning

While updating documentation, raise its version with update description in `book/versions.md` and in `book/package.json`
.
