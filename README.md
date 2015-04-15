# Spring Boot Sample

Spring Boot Web application to provide REST API in JSON

## 1. Getting started

### 1.1. Récupérer les sources

```
$ git clone git@github.com:AgileSpirit/spring-boot-sample.git
```

### 1.2. Lancer l'application

```
$ mvn spring-boot:run
```

### 1.3. API

Method | Path           | Description                    |
-------|----------------|--------------------------------|
GET    | /articles      | retrieve all the articles      |
GET    | /articles/{id} | retrieve one article by its ID |
POST   | /articles      | store a new article            |
PUT    | /articles      | update an existing article     |
DELETE | /articles/{id} | remove an article byt its ID   |

```
// GET /articles
$ curl -X GET http://localhost:8080/api/articles -i -H "Content-Type: application/json"

// GET /articles/{id}
$ curl -X GET http://localhost:8080/api/articles/1 -i -H "Content-Type: application/json"

// POST /articles
$ curl -X POST http://{host}:{port}/api/articles -i
    -H "Accept: application/json"
    -H "Content-Type: application/json"
    -d '{"title": "Hello world!", "author": "J. Doe", "publicationDate": "20/05/2015", "excerpt": "This is a simple hello world.", "content": "Lorem ipsum dolor sit amet etc."}'

// PUT /articles
$ curl -X PUT http://{host}:{port}/api/articles -i -H "Accept: application/json" -H "Content-Type: application/json"

// DELETE /articles/{id}
$ curl -X DELETE http://{host}:{port}/api/articles/{id} -i
```

### 1.4. IHM

Il est possible de voir une utilisation de l'API au travers une IHM web accessible à l'adresse : http://localhost:8080/articles.html


## 2. Tutoriel

### 2.1. Présentation générale

Spring Boot est un framework pour construire rapidement des applications Java/JEE riches (web ou standalone).

Spring Boot accélère le développement logiciel en proposant un ensemble de conventions, d'abstraction et de mécanismes prêt à l'emploi.

Concrètement, Spring Boot se présente sous la forme d'un POM parent et de dépendances -- a.k.a. des "starters" -- (Maven ou Gradle).

### 2.2. Objet du tutoriel

Dans ce tutoriel, nous allons voir comment :

- mettre en place, configurer et démarrer une application Web avec Spring Boot
- intégrer la librairie Lombok, pour éviter le code boilerplate (ex: getters / setters / equals / hashCode)
- déclarer une Entity JPA (pour la persistence) ainsi qu'un Repository associé complet (un CRUD complet !) en moins de 10 lignes (!!!)
- définir une Resource REST/JSON/HTTP (GET, POST, PUT, DELETE)
- faire des tests unitaires et d'intégration facilement avec Spring Test et MockMVC
- packager l'application à destination de serveurs J2E externes (ex: Tomcat / Jetty)

### 2.3. Initialiser un projet Web avec Spring Boot

Dans le réertoire de votre choix, créez l'arborescence suivante :

```
spring-boot-sample (${basedir})
  |
  +-- src
     |
     +-- main
     .  |
     .  +-- java
     .    |
     .    + com.acme.app
     .
     |
     +-- test
     .  |
     .  +-- java
     .    |
     .    + com.acme.app
```

Créez le fichier ```./pom.xml``` (pour Maven) :


```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <name>Spring Boot Sample</name>
    <groupId>com.acme.app</groupId>
    <artifactId>spring-boot-sample</artifactId>
    <version>1.0-SNAPSHOT</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.2.3.RELEASE</version>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

Créez la classe ```./src/main/java/com/acme/app/Application.java``` :

```
package com.acme.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

}
```

L'annotation ```@SpringBootApplication``` signifie qu'il s'agit d'une application Spring Boot.

L'instruction ```SpringApplication.run(Application.class, args);``` permet de lancer l'application via un serveur
embarqué (par défaut, Tomcat 7), directemnt depuis le main (clic-droit -> lancer l'application depuis la méthode main).


Lancez l'application :

- soit avec la ligne de commande : ```$ mvn spring-boot:run```
- soit depuis le main (cf. ci-dessus)

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.2.3.RELEASE)

2015-04-13 19:56:54.272  INFO 66176 --- [lication.main()] com.acme.app.Application                 : Starting Application on localhost with PID 66176 (/Users/Works/SpringBootSample/spring-boot-sample/target/classes started by jbuget in /Users/Works/SpringBootSample/spring-boot-sample)
2015-04-13 19:56:54.315  INFO 66176 --- [lication.main()] ationConfigEmbeddedWebApplicationContext : Refreshing org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext@4485b66d: startup date [Mon Apr 13 19:56:54 CEST 2015]; root of context hierarchy
2015-04-13 19:56:54.936  INFO 66176 --- [lication.main()] o.s.b.f.s.DefaultListableBeanFactory     : Overriding bean definition for bean 'beanNameViewResolver': replacing [Root bean: class [null]; scope=; abstract=false; lazyInit=false; autowireMode=3; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration$WhitelabelErrorViewConfiguration; factoryMethodName=beanNameViewResolver; initMethodName=null; destroyMethodName=(inferred); defined in class path resource [org/springframework/boot/autoconfigure/web/ErrorMvcAutoConfiguration$WhitelabelErrorViewConfiguration.class]] with [Root bean: class [null]; scope=; abstract=false; lazyInit=false; autowireMode=3; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration$WebMvcAutoConfigurationAdapter; factoryMethodName=beanNameViewResolver; initMethodName=null; destroyMethodName=(inferred); defined in class path resource [org/springframework/boot/autoconfigure/web/WebMvcAutoConfiguration$WebMvcAutoConfigurationAdapter.class]]
2015-04-13 19:56:55.645  INFO 66176 --- [lication.main()] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat initialized with port(s): 8080 (http)
2015-04-13 19:56:55.838  INFO 66176 --- [lication.main()] o.apache.catalina.core.StandardService   : Starting service Tomcat
2015-04-13 19:56:55.840  INFO 66176 --- [lication.main()] org.apache.catalina.core.StandardEngine  : Starting Servlet Engine: Apache Tomcat/8.0.20
2015-04-13 19:56:55.929  INFO 66176 --- [ost-startStop-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2015-04-13 19:56:55.929  INFO 66176 --- [ost-startStop-1] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 1618 ms
2015-04-13 19:56:56.628  INFO 66176 --- [ost-startStop-1] o.s.b.c.e.ServletRegistrationBean        : Mapping servlet: 'dispatcherServlet' to [/]
2015-04-13 19:56:56.632  INFO 66176 --- [ost-startStop-1] o.s.b.c.embedded.FilterRegistrationBean  : Mapping filter: 'characterEncodingFilter' to: [/*]
2015-04-13 19:56:56.632  INFO 66176 --- [ost-startStop-1] o.s.b.c.embedded.FilterRegistrationBean  : Mapping filter: 'hiddenHttpMethodFilter' to: [/*]
2015-04-13 19:56:56.852  INFO 66176 --- [lication.main()] s.w.s.m.m.a.RequestMappingHandlerAdapter : Looking for @ControllerAdvice: org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext@4485b66d: startup date [Mon Apr 13 19:56:54 CEST 2015]; root of context hierarchy
2015-04-13 19:56:56.911  INFO 66176 --- [lication.main()] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped "{[/error],methods=[],params=[],headers=[],consumes=[],produces=[text/html],custom=[]}" onto public org.springframework.web.servlet.ModelAndView org.springframework.boot.autoconfigure.web.BasicErrorController.errorHtml(javax.servlet.http.HttpServletRequest)
2015-04-13 19:56:56.912  INFO 66176 --- [lication.main()] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped "{[/error],methods=[],params=[],headers=[],consumes=[],produces=[],custom=[]}" onto public org.springframework.http.ResponseEntity<java.util.Map<java.lang.String, java.lang.Object>> org.springframework.boot.autoconfigure.web.BasicErrorController.error(javax.servlet.http.HttpServletRequest)
2015-04-13 19:56:56.934  INFO 66176 --- [lication.main()] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/**] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2015-04-13 19:56:56.934  INFO 66176 --- [lication.main()] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/webjars/**] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2015-04-13 19:56:56.973  INFO 66176 --- [lication.main()] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/**/favicon.ico] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2015-04-13 19:56:57.035  INFO 66176 --- [lication.main()] o.s.j.e.a.AnnotationMBeanExporter        : Registering beans for JMX exposure on startup
2015-04-13 19:56:57.090  INFO 66176 --- [lication.main()] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8080 (http)
2015-04-13 19:56:57.092  INFO 66176 --- [lication.main()] com.acme.app.Application                 : Started Application in 3.064 seconds (JVM running for 6.686)
```

### 2.4. Ajout de la librairie Lombok, pour éviter l'écriture de code BoilerPlate

Lombok est une lib Java bien pratique qui permet de s'économiser l'écriture de code BoilerPlate tel que : getters,
setters, méthodes equals / hascode, grâce à l'utilisation d'annotations (`@Data`, `@Getter`, `@Setter`,
`@equals`, @hashCode`, etc.).

Pour activer la librairie, il faut ajouter dans le fichier Maven pom.xml :

- le repository Lombok
- la dépendance Maven

```
<repositories>
    <repository>
        <id>projectlombok.org</id>
        <url>http://projectlombok.org/mavenrepo</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.16.2</version>
    </dependency>
</dependencies>

```

### 2.5 Déclaration du Modèle de Domaine

Notre modèle du domaine sera simple, à savoir, un simple objet Article (en vrai une Entité JPA), avec pour propriétés :

- l'identifiant de l'entité
- le titre de l'article
- le nom de l'auteur
- la date de publication
- un résumé de l'article
- le contenu de l'article

```
package com.acme.app;

import lombok.Data;

import java.io.Serializable;

@Data
public class Article implements Serializable {

    private Long id;
    private String title;
    private String author;
    private String publicationDate;
    private String excerpt;
    private String content;

}

```

La classe est préfixée `@Data` pour signifier à Lombok d'injecter de lui-même les méthodes boilerplate (getters /
setters / equals / hashCode) lors de la génération du bytecode Java.

### 2.6. Prise en compte de la persistence avec Hibernate / JPA / Spring Data JPA

Hibernate est l'ORM de référence du monde Java.

JPA est la norme Java standard couvrant la problématique de la persistence de données.

Spring Data JPA est une abstraction de JPA dans l'univers Spring. Par défaut, Spring Data JPA utilise Hibernate comme
implémentation de JPA.

#### 2.6.1. Déclaration de la dépendance Spring Data JPA pour Spring Boot

La première chose à faire est d'ajouter dans le pom.xml la dépendance du starter Spring Boot pour Spring Data JPA :

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

Il faut par ailleurs spécifier à Spring Boot quelle type de base de données sera utilisée. Dans notre cas, nous allons faire simple avec H2, une base SQL embarquée.

```
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>1.4.186</version>
    <scope>runtime</scope>
</dependency>
```
On notera au passage l'utilisation du scope `runtime` pour la dépendance H2.


#### 2.6.2. Mapping de l'Entity JPA

A présent que nous avons accès aux fonctionnalités JPA, nous pouvons "décorer" notre classe Article pour la rendre persistante :

```
package com.acme.app;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class Article implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String author;

    @Temporal(TemporalType.TIMESTAMP)
    private Date publicationDate;

    @Lob
    @Column(columnDefinition = "VARCHAR", length = 65535)
    private String excerpt;

    @Lob
    @Column(columnDefinition = "VARCHAR", length = 65535)
    private String content;

}
```

L'annotation `@Entity` indique que la classe Article est *managée* par JPA. Par défaut, la stratégie adoptée par Spring / Hibernate est d'associer une table SQL pour une entité JPA.

Les annotations `@Id` et `GeneratedValue` définissent respectivement la clé primaire de la table Article et la stratégie de gestion/génération de l'identifiant. Par défaut, les identifiants sont gérés automatiquement par le framework et générés au format `Number` de façon incrémentale (+1 à chaque nouvel Article).

L'annotation `@Temporal` permet d'indiquer au SGBD que le champ `publicationDate` est de type DateTime (date + time) (cf article [Dealing with Date, Time and Timestamp](http://www.developerscrappad.com/228/java/java-ee/ejb3-jpa-dealing-with-date-time-and-timestamp/ "Dealing with Date, Time and Timestamp")).

Les annotations `@Lob` et `@Column` permettent d'associer un type SQL `CLOB` aux attributs concernés.

Remarque : par défaut, Hibernate se base sur les getters pour faire le mapping Java / SQL. Dans notre cas, les getters sont générés par Lombok au moment de la génération du bytecode.

#### 2.6.3. Déclaration du Repository pour gérer les entités JPA

Un `Repository` est l'équivalent d'un objet DAO (Data Access Object) à la sauce DDD (Domain Driven Design). Le terme fait partie du vocabulaire choisi par Spring Data pour désigner un objet dont le rôle est d'accéder et de stocker des entités JPA via une `DataSource`.

L'une des fonctionnalités majeures de Spring Data JPA est de permettre de déclarer un objet Repository de CRUD complet pour une entité donnée en une seule ligne !

Pour ce faire, nous allons simplement créer l'interface ÀrticleRepository` :


```
package com.acme.app;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
```

La première chose à remarquer est qu'on déclare une interface plutôt qu'une classe : Spring et Spring Data vont se charger eux-même de l'injection de dépendance.

On remarque aussi qu'on étend de l'interface `JpaRepository` qui offre par défaut tout un tas de méthodes de CRUD telles que `#findOne()`, `#findAll()`, `#delete()`, `#save()`, etc.). Celle-ci prend en paramètres le type de l'entité concernée (ici la classe `Article`) et le type de l'ID (un `Long`).


### 2.7. Mise en place de l'API REST/JSON avec Spring MVC

#### 2.7.1. Déclaration de la Resource REST ArticleResource

A ce stade de l'application, nous avons un modèle de données et un repository (une DAO) pour l'alimenter et y accéder.

La prochaine étape consiste à créer la `Resource` (aussi appelé *Controller*) qui va exposer nos services REST.

Le premier service REST que nous allons créer et exposer est la méthode `GET /articles` qui permet de récupérer et de retourner tous les articles en base de données.

Pour cela, il faut ajouter la classe `ArticleResource` :

```
package com.acme.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleResource {

    @Autowired
    private ArticleRepository articleRepository;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Article> getArticles() {
        return articleRepository.findAll();
    }
}
```
L'annotation `@RestController` permet de déclarer notre classe comme étant une classe Controller (au sens Spring MVC) avec un comportement de ressource REST.

L'annotation `@RequestMapping`, placé au niveau de la classe, définie le chemin de base d'accès à la ressource. Tous les services situés au sein de la classe hériteront de ce chemin.

`@Autowired` permet d'injecter le `Repository` créée plus tôt (cf. section §2.6.3. ci-dessus).

On retrouve l'annotation `@RequestMapping` au niveau d'une métode cette fois. Elle nous permet de définir plus finement le service exposé (en l'occurence `#getArticles()`). L'argument `method` défini le verbe HTTP associé au service (NDLR : on aurait pu l'ommettre étant donné quel verbe par défaut proposé par Spring MVC est GET), et l'argument `produces` définie le format de la donnée qui sera émise (dans notre cas, de la donnée JSON au format media "application/json").

Remarque : le fait de définir l'attribut produces va ajouter dans la réponse HTTP le header Content-Type="application/json".
	
Enfin l'annotation @ResponseBody va signifier à Spring MVC qu'il doit effectuer une transformation JSON de l'objet passé en retour (ici, une liste d'Articles). Si on oulie cette annotation, alors Spring MVC tentera de retourner par défaut une String (au format "text/plain").

#### 2.7.2. Prérequis avant exécution

Pour pouvoir tester (en live, et pas encore de façon automatiser) notre ressource REST, nous devons au préalable ajouter des données dans l'application.

Pour ce faire, nous allons créer une méthode d'initialisation au lancement de l'application (après que Spring ce soit occupé de la création des beans et de l'injection de dépendances).

Dans la classe `Application`, il suffit de rajouter la méthode `#initialize()` annotée `PostConstruct`, laquelle va peupler la base de quelques articles.

```
package com.acme.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.Date;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private ArticleRepository articleRepository;

    @PostConstruct
    public void onStartup() {
        articleRepository.save(newArticle("Hello world !"));
        articleRepository.save(newArticle("Lorem ipsum dolor sit amet"));
        articleRepository.save(newArticle("Foo Bar Power"));
    }

    private Article newArticle(String title) {
        Article article = new Article();
        article.setTitle(title);
        article.setAuthor("J. Doe");
        article.setPublicationDate(new Date());
        article.setExcerpt("Ceci est un résumé de mon article");
        article.setContent("<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>");
        return article;
    }

}
```

#### 2.7.3. Vérification

On peut maintenant lancer l'application (`$ mvn spring-boot:run`) et accéder à l'URL [http://localhost:8080/articles](http://localhost:8080/articles "GET /articles") depuis notre navigateur pour voir remonter la liste des articles.

Et voilà ! Nous avons notre première ressource REST opérationnelle :-)

Nous pouvons maintenant implémenter les services restants, mais avant cela, nous allons voir comment...

### 2.8. Tester une application Spring Boot

#### 2.8.1. Ajout des dépendances Maven

Dans le POM Maven, il faut ajouter le start Spring Boot pour Spring Test :

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
</dependency>
```

Nous allons aussi ajouter une autre dépendance, [AssertJ](http://joel-costigliola.github.io/assertj/ "AssertJ"), qui est une librairie Java permettant d'écrire des assertions JUnit de façon courrante (*fluent*).

```
<dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <version>2.0.0</version>
    <scope>test</scope>
</dependency>
```

AssertJ permet par exemple d'écrire des assertions de ce type (extrait de la doc officielle) :

```
assertThat(frodo.getName()).isEqualTo("Frodo");

assertThat(frodo).isNotEqualTo(sauron)
                 .isIn(fellowshipOfTheRing);
                 
assertThat(frodo.getName()).startsWith("Fro")
                           .endsWith("do")
                           .isEqualToIgnoringCase("frodo");
```
                 
#### 2.8.2. Tests d'intégration partiel

Pour notre premier test, nous voulons quelque chose de simple, avec un contexte Spring initialisé et l'injection de dépendances réalisée.

Notre premier cas de test consiste à valider que l'injection de dépendances s'effectue bien, ainsi que le traitement `@PostConstruct`.

Pour cela, nous commençons par crééer la classe de test SpringConfigurationTest :

```
package com.acme.app;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class SpringConfigurationTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void testDependencyInjection() {
        Assertions.assertThat(articleRepository).isNotNull();
    }

    @Test
    public void testInitialize() {
        List<Article> articles = articleRepository.findAll();
        Assertions.assertThat(articles).isNotNull().hasSize(3);
    }

}
```

L'annotation `@RunWith(SpringJUnit4ClassRunner.class)` indique à JUnit que nos tests s'appuient sur Spring.

L'annotation `@SpringApplicationConfiguration(classes = Application.class)` indique au runner qu'il s'agit d'une application Spring Boot, dont le point d'entrée (qui contient la configuration) est la classe `Application`. Le runner va se baser sur cette dernière pour charger le contexte Spring.

L'annotation `@Autowired` permet de récupèrer depuis le contexte Spring notre `ArticleRepository`.

L'annotation `@Test` indique à JUnit que la méthode décorée est un scénario de test.

Le premier test -- #testDependencyInjection() -- vérifie que l'injection de dépendances est OK. Le second -- #testInitialize() -- vérifie que la méthode `Application#initialize()` a bien été appelée.

Pour lancer le test, il suffit :

- d'exécuter la commande Maven `$ mvn test`
- ou bien de lancer le test depuis votre IDE (clic-droit sur la classe de test -> "lancer en tant que test JUnit")

Vous devriez avoir la *stack trace* suivante :

```


  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.2.3.RELEASE)

2015-04-15 09:01:16.032  INFO 70940 --- [           main] c.i.rt.execution.junit.JUnitStarter      : Starting JUnitStarter on localhost with PID 70940 (started by jbuget in /Users/Works/SpringBootSample/spring-boot-sample)
2015-04-15 09:01:16.070  INFO 70940 --- [           main] s.c.a.AnnotationConfigApplicationContext : Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@66b2cd4f: startup date [Wed Apr 15 09:01:16 CEST 2015]; root of context hierarchy
2015-04-15 09:01:18.261  INFO 70940 --- [           main] j.LocalContainerEntityManagerFactoryBean : Building JPA container EntityManagerFactory for persistence unit 'default'
2015-04-15 09:01:19.658  INFO 70940 --- [           main] c.i.rt.execution.junit.JUnitStarter      : Started JUnitStarter in 3.909 seconds (JVM running for 4.456)
2015-04-15 09:01:19.861  INFO 70940 --- [       Thread-1] s.c.a.AnnotationConfigApplicationContext : Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@66b2cd4f: startup date [Wed Apr 15 09:01:16 CEST 2015]; root of context hierarchy
2015-04-15 09:01:19.865  INFO 70940 --- [       Thread-1] j.LocalContainerEntityManagerFactoryBean : Closing JPA EntityManagerFactory for persistence unit 'default'

Process finished with exit code 0
```


#### 2.8.3. Tests d'intégration complet

Imaginons qu'on veuille à présent tester automatiquement notre application dans des conditions proches du réel, c'est-à-dire comme si elle tournait, par exemple, dans le cadre de tests fonctionnels (Cucumber, JBehave, Fitnesse).

Depuis la version 1.2.1, Spring Boot propose l'annotation `@WebIntegrationTest` qui permet de lancer l'application sur un port (qui peut être spécifié) et d'y accéder.

Remarque : Spring fait bien les choses et met en cache le contexte ainsi que le nécessaire pour ne pas avoir à redémarrer l'application complètement à chaque Test Case ou Test Suite.

Créez la classe `SpringIntegrationTest` suivante :

```
package com.acme.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest("server.port:0")
public class SpringIntegrationTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringIntegrationTest.class);

    @Value("${local.server.port}")
    private int port;

    @Test
    public void testApplication() {
        LOGGER.info("L'application tourne sur le port : " + port);
    }

}
```

Maintenant, si vous exécutez le test, vous devriez avoir une stack trace une peu différente :

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.2.3.RELEASE)

2015-04-15 09:09:08.725  INFO 71016 --- [           main] c.i.rt.execution.junit.JUnitStarter      : Starting JUnitStarter on localhost with PID 71016 (started by jbuget in /Users/Works/SpringBootSample/spring-boot-sample)
2015-04-15 09:09:08.778  INFO 71016 --- [           main] ationConfigEmbeddedWebApplicationContext : Refreshing org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext@3f15f21b: startup date [Wed Apr 15 09:09:08 CEST 2015]; root of context hierarchy
2015-04-15 09:09:09.958  INFO 71016 --- [           main] o.s.b.f.s.DefaultListableBeanFactory     : Overriding bean definition for bean 'beanNameViewResolver': replacing [Root bean: class [null]; scope=; abstract=false; lazyInit=false; autowireMode=3; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration$WhitelabelErrorViewConfiguration; factoryMethodName=beanNameViewResolver; initMethodName=null; destroyMethodName=(inferred); defined in class path resource [org/springframework/boot/autoconfigure/web/ErrorMvcAutoConfiguration$WhitelabelErrorViewConfiguration.class]] with [Root bean: class [null]; scope=; abstract=false; lazyInit=false; autowireMode=3; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration$WebMvcAutoConfigurationAdapter; factoryMethodName=beanNameViewResolver; initMethodName=null; destroyMethodName=(inferred); defined in class path resource [org/springframework/boot/autoconfigure/web/WebMvcAutoConfiguration$WebMvcAutoConfigurationAdapter.class]]
2015-04-15 09:09:10.764  INFO 71016 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'org.springframework.transaction.annotation.ProxyTransactionManagementConfiguration' of type [class org.springframework.transaction.annotation.ProxyTransactionManagementConfiguration$$EnhancerBySpringCGLIB$$adf948e] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
2015-04-15 09:09:10.784  INFO 71016 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'transactionAttributeSource' of type [class org.springframework.transaction.annotation.AnnotationTransactionAttributeSource] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
2015-04-15 09:09:10.793  INFO 71016 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'transactionInterceptor' of type [class org.springframework.transaction.interceptor.TransactionInterceptor] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
2015-04-15 09:09:10.797  INFO 71016 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'org.springframework.transaction.config.internalTransactionAdvisor' of type [class org.springframework.transaction.interceptor.BeanFactoryTransactionAttributeSourceAdvisor] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
2015-04-15 09:09:11.248  INFO 71016 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat initialized with port(s): 0 (http)
2015-04-15 09:09:11.488  INFO 71016 --- [           main] o.apache.catalina.core.StandardService   : Starting service Tomcat
2015-04-15 09:09:11.490  INFO 71016 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet Engine: Apache Tomcat/8.0.20
2015-04-15 09:09:11.644  INFO 71016 --- [ost-startStop-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2015-04-15 09:09:11.644  INFO 71016 --- [ost-startStop-1] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 2868 ms
2015-04-15 09:09:12.429  INFO 71016 --- [ost-startStop-1] o.s.b.c.e.ServletRegistrationBean        : Mapping servlet: 'dispatcherServlet' to [/]
2015-04-15 09:09:12.435  INFO 71016 --- [ost-startStop-1] o.s.b.c.embedded.FilterRegistrationBean  : Mapping filter: 'characterEncodingFilter' to: [/*]
2015-04-15 09:09:12.435  INFO 71016 --- [ost-startStop-1] o.s.b.c.embedded.FilterRegistrationBean  : Mapping filter: 'hiddenHttpMethodFilter' to: [/*]
2015-04-15 09:09:13.030  INFO 71016 --- [           main] j.LocalContainerEntityManagerFactoryBean : Building JPA container EntityManagerFactory for persistence unit 'default'
2015-04-15 09:09:14.415  INFO 71016 --- [           main] s.w.s.m.m.a.RequestMappingHandlerAdapter : Looking for @ControllerAdvice: org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext@3f15f21b: startup date [Wed Apr 15 09:09:08 CEST 2015]; root of context hierarchy
2015-04-15 09:09:14.487  INFO 71016 --- [           main] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped "{[/articles],methods=[GET],params=[],headers=[],consumes=[],produces=[application/json],custom=[]}" onto public java.util.List<com.acme.app.Article> com.acme.app.ArticleResource.getArticles()
2015-04-15 09:09:14.490  INFO 71016 --- [           main] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped "{[/error],methods=[],params=[],headers=[],consumes=[],produces=[text/html],custom=[]}" onto public org.springframework.web.servlet.ModelAndView org.springframework.boot.autoconfigure.web.BasicErrorController.errorHtml(javax.servlet.http.HttpServletRequest)
2015-04-15 09:09:14.490  INFO 71016 --- [           main] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped "{[/error],methods=[],params=[],headers=[],consumes=[],produces=[],custom=[]}" onto public org.springframework.http.ResponseEntity<java.util.Map<java.lang.String, java.lang.Object>> org.springframework.boot.autoconfigure.web.BasicErrorController.error(javax.servlet.http.HttpServletRequest)
2015-04-15 09:09:14.524  INFO 71016 --- [           main] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/**] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2015-04-15 09:09:14.525  INFO 71016 --- [           main] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/webjars/**] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2015-04-15 09:09:14.578  INFO 71016 --- [           main] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/**/favicon.ico] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2015-04-15 09:09:14.773  INFO 71016 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 51405 (http)
2015-04-15 09:09:14.776  INFO 71016 --- [           main] c.i.rt.execution.junit.JUnitStarter      : Started JUnitStarter in 6.401 seconds (JVM running for 7.246)
2015-04-15 09:09:14.785  INFO 71016 --- [           main] com.acme.app.SpringIntegrationTest       : L'application tourne sur le port : 51405
2015-04-15 09:09:14.788  INFO 71016 --- [       Thread-1] ationConfigEmbeddedWebApplicationContext : Closing org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext@3f15f21b: startup date [Wed Apr 15 09:09:08 CEST 2015]; root of context hierarchy
2015-04-15 09:09:14.795  INFO 71016 --- [       Thread-1] j.LocalContainerEntityManagerFactoryBean : Closing JPA EntityManagerFactory for persistence unit 'default'

Process finished with exit code 0
```

#### 2.8.4. Tests d'intégration en mode "bouchon"





