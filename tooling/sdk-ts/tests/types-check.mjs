// Checks the public types with a real TypeScript compiler, which this project does not depend on: the sample of mistakes the types
// have to catch, and every TypeScript block of the guide.
//
// Usage: node types-check.mjs <path of typescript.js> <directory of the package>. The environment gives CALENDRUM_GUIDE.
import { copyFileSync, mkdirSync, mkdtempSync, readFileSync, realpathSync, rmSync, symlinkSync, writeFileSync } from "node:fs";
import { createRequire } from "node:module";
import { tmpdir } from "node:os";
import { dirname, join, resolve } from "node:path";
import { fileURLToPath } from "node:url";

const here = dirname(fileURLToPath(import.meta.url));
const ts = createRequire(import.meta.url)(resolve(process.argv[2]));
const packageDirectory = realpathSync(resolve(process.argv[3]));
const guide = readFileSync(process.env.CALENDRUM_GUIDE, "utf8");
const blocks = [...guide.matchAll(/^```typescript\n([\s\S]*?)^```$/gm)].map((match) => match[1]);

const directory = realpathSync(mkdtempSync(join(tmpdir(), "calendrum-")));
let failed = false;
try {
    mkdirSync(join(directory, "node_modules"));
    symlinkSync(packageDirectory, join(directory, "node_modules", "calendrum"), "dir");
    writeFileSync(join(directory, "package.json"), '{"type":"module"}');
    const files = [join(directory, "types-sample.ts")];
    copyFileSync(join(here, "types-sample.ts"), files[0]);
    blocks.forEach((block, index) => {
        const file = join(directory, `guide${index}.ts`);
        writeFileSync(file, block);
        files.push(file);
    });
    const options = {
        strict: true,
        noEmit: true,
        skipLibCheck: false,
        noUncheckedIndexedAccess: true,
        exactOptionalPropertyTypes: false,
        allowImportingTsExtensions: true,
        target: ts.ScriptTarget.ES2022,
        module: ts.ModuleKind.Node16,
        moduleResolution: ts.ModuleResolutionKind.Node16,
        lib: ["lib.es2023.d.ts", "lib.dom.d.ts", "lib.esnext.disposable.d.ts"],
        types: [],
    };
    const program = ts.createProgram(files, options);
    const diagnostics = ts.getPreEmitDiagnostics(program);
    for (const diagnostic of diagnostics) {
        const where = diagnostic.file === undefined ? "" : `${diagnostic.file.fileName.replace(directory, "")}:${diagnostic.file.getLineAndCharacterOfPosition(diagnostic.start).line + 1}: `;
        console.log(`${where}${ts.flattenDiagnosticMessageText(diagnostic.messageText, "\n")}`);
    }
    const declarations = program.getSourceFiles().filter((file) => file.fileName.startsWith(packageDirectory) && file.fileName.endsWith(".d.mts")).length;
    console.log(`TypeScript ${ts.version}: ${files.length} files, ${declarations} declaration files of the package, ${diagnostics.length} errors`);
    failed = diagnostics.length > 0 || declarations < 4;
} finally {
    rmSync(directory, { recursive: true, force: true });
}
if (failed) process.exit(1);
