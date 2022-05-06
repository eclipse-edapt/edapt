# Eclipse Edapt™ - Migrating EMF Models

Eclipse Edapt™ is an EMF framework to migrate instances of Ecore models after changing an Ecore. This is typically used to migrate existing EMF-based data with a new release of a software with an updated Ecore model.

## Documentation

The official documentation is available on the website [eclipse.org/edapt](https://www.eclipse.org/edapt/documentation.php)

## Feedback, Help, and Support

See the [CONTRIBUTING](CONTRIBUTING) file.

## Build

The maven tycho build can be invoked with

``` bash
mvn clean verify -f builds/org.eclipse.emf.edapt.releng/pom.xml

# if you run into problems with eclipse p2 mirror sites, try disabling mirrors
mvn clean verify -f builds/org.eclipse.emf.edapt.releng/pom.xml -Dtycho.disableP2Mirrors=true
```

The Bundle-RequiredExecutionEnvironment is JavaSE-1.6

The p2-repository is created at `builds/org.eclipse.emf.edapt.releng.p2/target/repository`

## License

Edapt is licensed under Eclipse Public License - v 2.0. See the [LICENSE](LICENSE) file for more information.
