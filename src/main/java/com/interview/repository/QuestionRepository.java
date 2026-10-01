package com.interview.repository;

import com.interview.model.DifficultyLevel;
import com.interview.model.JobRole;
import com.interview.model.Question;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class QuestionRepository {

    private final Map<String, Question> questionStore = new ConcurrentHashMap<>();

    public QuestionRepository() {
        initQuestionBank();
    }

    public List<Question> findByRoleAndDifficulty(JobRole role, DifficultyLevel difficulty) {
        return questionStore.values().stream()
                .filter(q -> q.getRole() == role && q.getDifficulty() == difficulty)
                .collect(Collectors.toList());
    }

    public Question findById(String id) {
        return questionStore.get(id);
    }

    public List<Question> getAllQuestions() {
        return new ArrayList<>(questionStore.values());
    }

    private void add(Question q) {
        questionStore.put(q.getId(), q);
    }

    private void initQuestionBank() {
        // =========================================================================
        // 1. JAVA DEVELOPER
        // =========================================================================
        // Beginner (5 questions)
        add(new Question(
                "JAVA-BEG-01", JobRole.JAVA_DEVELOPER, DifficultyLevel.BEGINNER, "Core Java & OOP",
                "Explain the four main pillars of Object-Oriented Programming (OOP) in Java and give a brief real-world example.",
                List.of("encapsulation", "inheritance", "polymorphism", "abstraction"),
                List.of("encapsulation hides internal state", "inheritance allows code reuse", "polymorphism enables multiple behaviors", "abstraction exposes only essential features"),
                "The four pillars are Encapsulation (data hiding with private fields and getters/setters), Inheritance (extending classes for reusability), Polymorphism (method overloading and overriding), and Abstraction (hiding implementation details using interfaces and abstract classes).",
                "Can you specifically differentiate between method overloading and method overriding in Polymorphism?",
                List.of("polymorphism", "overriding", "overloading", "compile-time", "runtime")
        ));

        add(new Question(
                "JAVA-BEG-02", JobRole.JAVA_DEVELOPER, DifficultyLevel.BEGINNER, "Memory Management",
                "What is the difference between the Stack and the Heap memory in the Java Virtual Machine (JVM)?",
                List.of("stack", "heap", "primitive", "object", "garbage collection"),
                List.of("stack stores method frames and local variables", "heap stores objects and instance variables", "garbage collector cleans heap memory"),
                "Stack memory is used for static memory allocation and execution of threads, storing primitive local variables and references to objects. Heap memory is used for dynamic memory allocation where all objects and JRE classes reside, managed by Garbage Collection.",
                "How does the JVM Garbage Collector know when an object in the Heap is eligible for collection?",
                List.of("gc", "garbage", "unreachable", "reference", "roots")
        ));

        add(new Question(
                "JAVA-BEG-03", JobRole.JAVA_DEVELOPER, DifficultyLevel.BEGINNER, "Java Fundamentals",
                "What is the difference between '==' and the '.equals()' method when comparing objects in Java?",
                List.of("==", "equals", "reference", "memory address", "content", "value"),
                List.of("== checks memory reference equality", "equals checks logical content equivalence", "overriding equals and hashcode"),
                "The '==' operator checks reference equality, verifying whether both references point to the exact same memory address. The '.equals()' method checks logical or content equality, and can be overridden by classes like String or custom domain objects to compare values.",
                "Why is it recommended to override 'hashCode()' whenever you override 'equals()' in Java?",
                List.of("hashcode", "hashmap", "contract", "bucket", "collection")
        ));

        add(new Question(
                "JAVA-BEG-04", JobRole.JAVA_DEVELOPER, DifficultyLevel.BEGINNER, "Collections Framework",
                "Compare ArrayList and LinkedList in Java. In what scenarios would you choose one over the other?",
                List.of("arraylist", "linkedlist", "dynamic array", "doubly linked", "index", "traversal", "insertion"),
                List.of("arraylist uses dynamic array with O(1) random access", "linkedlist uses doubly-linked nodes with O(1) insertions at ends", "arraylist preferred for frequent reads"),
                "ArrayList is backed by a resizable array offering O(1) constant time random access by index, making it ideal for read-heavy operations. LinkedList is backed by a doubly linked list where elements are node pointers, offering O(1) insertion/deletion at ends without resizing, but O(n) indexed lookups.",
                "How does ArrayList handle resizing when its internal array capacity is exceeded?",
                List.of("capacity", "resize", "growth", "copy", "new array")
        ));

        add(new Question(
                "JAVA-BEG-05", JobRole.JAVA_DEVELOPER, DifficultyLevel.BEGINNER, "Spring Framework",
                "What is Inversion of Control (IoC) and Dependency Injection (DI) in Spring, and how do they benefit developers?",
                List.of("inversion of control", "dependency injection", "ioc container", "loose coupling", "beans", "autowired"),
                List.of("ioc delegates object lifecycle to container", "di injects dependencies rather than hardcoding", "promotes loose coupling and testability"),
                "Inversion of Control (IoC) transfers the control of object creation and lifecycle management to the Spring Container. Dependency Injection (DI) is the design pattern implementing IoC, where the container injects dependent objects via constructor or setter injection, leading to loose coupling and easy unit testing.",
                "Which form of Dependency Injection is preferred in modern Spring: Constructor Injection or Field Injection, and why?",
                List.of("constructor", "field", "immutability", "testing", "nullpointer")
        ));

        add(new Question(
                "JAVA-BEG-06", JobRole.JAVA_DEVELOPER, DifficultyLevel.BEGINNER, "Exception Handling",
                "Explain the difference between Checked and Unchecked exceptions in Java and how try-with-resources improves resource management.",
                List.of("checked", "unchecked", "runtimeexception", "autocloseable", "try-with-resources", "finally"),
                List.of("checked exceptions are checked at compile time", "unchecked inherit from RuntimeException", "try-with-resources automatically closes AutoCloseable resources"),
                "Checked exceptions must be explicitly declared or caught at compile-time (e.g., IOException, SQLException). Unchecked exceptions extend RuntimeException and represent programming bugs (e.g., NullPointerException). Java 7 introduced try-with-resources, which automatically closes resources implementing AutoCloseable without manual finally blocks.",
                "Can you create custom checked and unchecked exceptions in Java? Which base classes would you extend?",
                List.of("custom", "exception", "runtimeexception", "extend", "subclass")
        ));

        // Intermediate (5 questions)
        add(new Question(
                "JAVA-INT-01", JobRole.JAVA_DEVELOPER, DifficultyLevel.INTERMEDIATE, "Concurrency & Multithreading",
                "Explain how the Java Memory Model ensures thread safety with 'volatile', 'synchronized', and atomic classes.",
                List.of("volatile", "synchronized", "visibility", "atomicity", "thread safety", "happens-before"),
                List.of("volatile guarantees visibility across CPU caches", "synchronized guarantees mutual exclusion and atomicity", "atomic classes use CAS without locking"),
                "The 'volatile' keyword guarantees visibility across threads by reading/writing directly from main memory rather than CPU caches. 'synchronized' provides both visibility and atomicity via intrinsic locks (monitors). Atomic classes (e.g., AtomicInteger) achieve lock-free thread safety using CPU-level Compare-And-Swap (CAS).",
                "Can 'volatile' replace 'synchronized' for increment operations like count++? Why or why not?",
                List.of("atomicity", "compound", "race condition", "cas", "read-modify-write")
        ));

        add(new Question(
                "JAVA-INT-02", JobRole.JAVA_DEVELOPER, DifficultyLevel.INTERMEDIATE, "Spring Boot & Microservices",
                "How does Spring Boot auto-configuration work under the hood using annotations like @EnableAutoConfiguration and condition annotations?",
                List.of("auto-configuration", "spring.factories", "autoconfiguration.imports", "@conditional", "condition", "classpath"),
                List.of("scans classpath for starter libraries", "evaluates @ConditionalOnClass and @ConditionalOnMissingBean", "registers default beans automatically"),
                "Spring Boot auto-configuration checks META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports. It evaluates conditional annotations such as @ConditionalOnClass, @ConditionalOnMissingBean, and @ConditionalOnProperty to register sensible default beans unless customized by the developer.",
                "What is the purpose of @ConditionalOnMissingBean and how does it empower developers?",
                List.of("override", "custom", "bean", "default", "fallback")
        ));

        add(new Question(
                "JAVA-INT-03", JobRole.JAVA_DEVELOPER, DifficultyLevel.INTERMEDIATE, "Java 8+ Features",
                "Explain the difference between intermediate and terminal operations in Java Streams with practical examples.",
                List.of("stream", "intermediate", "terminal", "lazy", "filter", "map", "collect"),
                List.of("intermediate operations are lazy and return a new stream", "terminal operations produce a result or side-effect and trigger execution", "pipeline optimization"),
                "Intermediate operations (such as filter, map, sorted) transform a stream into another stream and are evaluated lazily. Terminal operations (such as collect, forEach, reduce, count) initiate stream processing, traverse elements, and produce a non-stream result or side-effect.",
                "What happens if you invoke multiple terminal operations on the same Java Stream instance?",
                List.of("illegalstateexception", "consumed", "closed", "reuse", "single use")
        ));

        add(new Question(
                "JAVA-INT-04", JobRole.JAVA_DEVELOPER, DifficultyLevel.INTERMEDIATE, "Database & JPA",
                "What is the N+1 select problem in Hibernate/JPA, and what are the best strategies to resolve it?",
                List.of("n+1", "hibernate", "lazy loading", "join fetch", "entity graph", "batch"),
                List.of("fetching 1 parent causes n queries for children", "solve using JOIN FETCH in JPQL", "solve using @EntityGraph or hibernate batch size"),
                "The N+1 problem occurs when fetching a list of N entities with lazy associations triggers 1 initial query for the parent records plus N additional queries to fetch each child relation. Solutions include using 'JOIN FETCH' in JPQL, utilizing JPA Entity Graphs, or configuring batch fetching with @BatchSize.",
                "What is the trade-off of using eager loading (FetchType.EAGER) everywhere instead of lazy loading?",
                List.of("memory", "performance", "unnecessary", "cartesian product", "latency")
        ));

        add(new Question(
                "JAVA-INT-05", JobRole.JAVA_DEVELOPER, DifficultyLevel.INTERMEDIATE, "Design Patterns",
                "How is the Spring Bean lifecycle managed from instantiation to destruction, including BeanPostProcessors?",
                List.of("lifecycle", "instantiation", "populate", "beanpostprocessor", "init-method", "postconstruct", "destroy"),
                List.of("instantiate bean instance", "inject dependencies and properties", "run BeanPostProcessor before/after initialization", "call @PostConstruct and destroy methods"),
                "The Spring Bean lifecycle begins with instantiation and property injection, followed by Aware interfaces. Next, BeanPostProcessor postProcessBeforeInitialization runs, followed by @PostConstruct / initializing callbacks. Then postProcessAfterInitialization executes (often generating AOP proxies), and finally @PreDestroy upon container shutdown.",
                "How does Spring AOP leverage BeanPostProcessors to create dynamic proxies?",
                List.of("proxy", "cglib", "dynamic proxy", "wrapper", "cross-cutting")
        ));

        // Advanced (5 questions)
        add(new Question(
                "JAVA-ADV-01", JobRole.JAVA_DEVELOPER, DifficultyLevel.ADVANCED, "JVM Internals & Garbage Collection",
                "Compare the G1 Garbage Collector with ZGC / Shenandoah in high-throughput, low-latency Java applications.",
                List.of("g1", "zgc", "shenandoah", "pause time", "concurrent", "colored pointers", "stop-the-world"),
                List.of("g1 divides heap into regions with predictable pause targets", "zgc achieves sub-millisecond pauses concurrently using load barriers", "tradeoffs between throughput and latency"),
                "G1GC partitions heap into regions and performs generational garbage collection aiming for predictable pause time targets. ZGC and Shenandoah are concurrent, ultra-low latency collectors performing memory compaction and evacuation concurrently with application threads using load barriers and colored pointers, maintaining sub-millisecond pause times even on multi-terabyte heaps.",
                "What is the memory footprint and CPU throughput overhead trade-off when choosing ZGC over Parallel GC?",
                List.of("throughput", "cpu overhead", "load barrier", "tradeoff", "overhead")
        ));

        add(new Question(
                "JAVA-ADV-02", JobRole.JAVA_DEVELOPER, DifficultyLevel.ADVANCED, "Distributed Systems & Transactions",
                "How do you implement distributed transactions across microservices in Spring Boot without 2-Phase Commit (2PC)?",
                List.of("saga", "choreography", "orchestration", "compensating transaction", "outbox pattern", "eventual consistency"),
                List.of("saga pattern breaks transaction into local transactions", "compensating actions undo partial steps", "transactional outbox guarantees reliable message delivery"),
                "In microservices, distributed transactions are managed via the Saga Pattern instead of 2PC. A Saga coordinates local transactions using either Orchestration (central orchestrator) or Choreography (event-driven). If a step fails, compensating transactions are triggered in reverse. The Transactional Outbox pattern ensures dual-write reliability between DB and message broker.",
                "How do you handle idempotency for compensating transactions in a distributed Saga?",
                List.of("idempotency", "idempotency key", "duplicate", "deduplication", "replay")
        ));

        add(new Question(
                "JAVA-ADV-03", JobRole.JAVA_DEVELOPER, DifficultyLevel.ADVANCED, "Modern Java & Virtual Threads",
                "Explain Project Loom Virtual Threads in Java 21. How do they fundamentally change high-concurrency I/O compared to reactive programming (WebFlux)?",
                List.of("virtual threads", "project loom", "carrier threads", "blocking", "reactive", "continuation", "context switch"),
                List.of("virtual threads are lightweight user-mode threads scheduled on carrier threads", "allow simple synchronous blocking code without thread exhaustion", "replaces complex reactive chains for standard I/O"),
                "Virtual threads in Java 21 are lightweight user-mode threads managed by the JVM rather than the OS kernel. When a virtual thread performs blocking I/O, it unmounts from its carrier platform thread, allowing high throughput with traditional imperative code. This eliminates the callback complexity and difficult debugging associated with reactive frameworks like WebFlux.",
                "What is 'pinning' in Virtual Threads, and how can synchronized blocks cause it?",
                List.of("pinning", "synchronized", "reentrantlock", "carrier thread", "blocking")
        ));

        add(new Question(
                "JAVA-ADV-04", JobRole.JAVA_DEVELOPER, DifficultyLevel.ADVANCED, "High Performance & Profiling",
                "How would you diagnose and resolve a severe off-heap memory leak or Metaspace exhaustion in a production Spring Boot service?",
                List.of("metaspace", "off-heap", "direct byte buffer", "profiler", "jcmd", "jfr", "async-profiler"),
                List.of("differentiate heap from off-heap using jcmd and Native Memory Tracking (NMT)", "inspect class loaders for metaspace leaks caused by dynamic proxies", "analyze direct memory buffers with JFR"),
                "To diagnose Metaspace leaks, inspect classloaders using jcmd VM.metaspace and check for uncollected dynamic classes from libraries like CGLIB. For off-heap leaks, enable Native Memory Tracking (-XX:NativeMemoryTracking=summary) and analyze with Java Flight Recorder (JFR) or async-profiler to spot leaked DirectByteBuffers or JNI allocations.",
                "What flag would you enable to monitor Native Memory Tracking with minimal production overhead?",
                List.of("nativememorytracking", "nmt", "summary", "baseline", "diff")
        ));

        add(new Question(
                "JAVA-ADV-05", JobRole.JAVA_DEVELOPER, DifficultyLevel.ADVANCED, "Resilience & Security",
                "How do you implement the Circuit Breaker and Bulkhead patterns in Spring Boot using Resilience4j to protect downstream services?",
                List.of("resilience4j", "circuit breaker", "bulkhead", "half-open", "fallback", "thread pool", "failure rate"),
                List.of("circuit breaker trips open when failure rate exceeds threshold", "half-open allows test calls before closing", "bulkhead isolates thread pools and resource quotas to prevent cascade failures"),
                "Resilience4j provides lightweight decorators. The Circuit Breaker monitors call failure rates across a sliding window; when the threshold is crossed, it transitions to OPEN to reject calls and invoke fallbacks, then HALF-OPEN to test recovery. The Bulkhead pattern isolates resources (thread pools or semaphores) to ensure one failing downstream service cannot starve threads for the rest of the application.",
                "How do you distinguish between transient network errors that should trip a circuit breaker and client 4xx errors?",
                List.of("recordexceptions", "ignoreexceptions", "predicate", "status code", "client error")
        ));

        // =========================================================================
        // 2. PYTHON DEVELOPER
        // =========================================================================
        // Beginner (5)
        add(new Question(
                "PY-BEG-01", JobRole.PYTHON_DEVELOPER, DifficultyLevel.BEGINNER, "Core Python",
                "What is the difference between mutable and immutable types in Python? Provide two examples of each.",
                List.of("mutable", "immutable", "list", "tuple", "string", "dictionary", "id"),
                List.of("mutable objects can be modified in place", "immutable objects cannot be changed after creation", "examples: list and dict vs int, str, tuple"),
                "Mutable types can have their state or contents modified in-place without changing their memory id (e.g., lists, dictionaries, sets). Immutable types cannot be changed once created; modifications create a new object in memory (e.g., strings, tuples, integers).",
                "What happens when you use a mutable object (like a list) as a default argument in a Python function definition?",
                List.of("default argument", "shared", "persistent", "none", "side effect")
        ));

        add(new Question(
                "PY-BEG-02", JobRole.PYTHON_DEVELOPER, DifficultyLevel.BEGINNER, "Data Structures",
                "Explain the difference between a Python List and a Tuple. When should you choose a Tuple?",
                List.of("list", "tuple", "mutable", "immutable", "parentheses", "brackets", "hashable"),
                List.of("lists are mutable while tuples are immutable", "tuples have lower memory overhead", "tuples can be used as dictionary keys because they are hashable"),
                "Lists are mutable and enclosed in square brackets [], while tuples are immutable and defined with parentheses (). Tuples should be chosen for fixed collections, data integrity, dict keys (since they are hashable), and slight performance/memory advantages.",
                "Can a tuple contain a mutable list as one of its elements? Can that tuple still be used as a dictionary key?",
                List.of("hashable", "typeerror", "unhashable", "list", "nested")
        ));

        add(new Question(
                "PY-BEG-03", JobRole.PYTHON_DEVELOPER, DifficultyLevel.BEGINNER, "Functions & Iterables",
                "What are Python Generators and the 'yield' keyword? How do they improve memory efficiency?",
                List.of("generator", "yield", "lazy", "memory", "iterator", "stream"),
                List.of("yield returns value lazily without terminating function", "generators produce values one at a time on demand", "avoids loading entire datasets into memory"),
                "Generators are functions that yield values lazily one at a time using the 'yield' keyword instead of returning a complete collection. They maintain their execution state between iterations, providing O(1) memory consumption regardless of the dataset size.",
                "How do you manually advance a generator or retrieve its next element?",
                List.of("next", "stopiteration", "loop", "advance", "iterator")
        ));

        add(new Question(
                "PY-BEG-04", JobRole.PYTHON_DEVELOPER, DifficultyLevel.BEGINNER, "Language Mechanics",
                "Explain Python's *args and **kwargs syntax in function definitions with an example of where they are useful.",
                List.of("args", "kwargs", "positional", "keyword", "unpacking", "tuple", "dict"),
                List.of("*args gathers variable positional arguments into a tuple", "**kwargs gathers variable keyword arguments into a dictionary", "enables flexible function signatures and decorator wrappers"),
                "*args allows a function to accept any number of positional arguments, packing them into a tuple. **kwargs accepts arbitrary keyword arguments as a dictionary. They are essential for writing decorators, wrapper functions, and flexible APIs.",
                "In what order must regular arguments, *args, default arguments, and **kwargs appear in a function signature?",
                List.of("order", "positional", "keyword", "syntax", "precedence")
        ));

        add(new Question(
                "PY-BEG-05", JobRole.PYTHON_DEVELOPER, DifficultyLevel.BEGINNER, "Context Managers",
                "What does the 'with' statement do in Python, and how does it manage resources like files?",
                List.of("with", "context manager", "__enter__", "__exit__", "resource", "close", "exception"),
                List.of("guarantees cleanup like closing files or releasing locks", "implements __enter__ and __exit__ protocol", "executes exit even if exceptions occur"),
                "The 'with' statement creates a context manager that guarantees proper resource acquisition and cleanup (like closing file descriptors or releasing database locks). It invokes __enter__() upon entering the block and guarantees __exit__() execution even if an exception is raised.",
                "How would you create a custom context manager using Python's 'contextlib' module?",
                List.of("contextmanager", "decorator", "yield", "generator", "try finally")
        ));

        // Intermediate (5)
        add(new Question(
                "PY-INT-01", JobRole.PYTHON_DEVELOPER, DifficultyLevel.INTERMEDIATE, "Advanced Python",
                "What are Python Decorators, and how do you write a decorator that accepts custom configuration arguments?",
                List.of("decorator", "wrapper", "higher-order", "functools", "wraps", "closure"),
                List.of("decorators wrap a function to extend behavior without modifying it", "nested function closure for arguments", "use functools.wraps to preserve function metadata"),
                "A decorator is a callable that takes another function and extends its behavior. A decorator with arguments requires three nested functions: the outer accepts arguments, the middle receives the target function, and the inner wrapper executes logic. Using @functools.wraps ensures docstrings and function names are preserved.",
                "Why is @functools.wraps important when writing production decorators?",
                List.of("metadata", "docstring", "name", "introspection", "debugging")
        ));

        add(new Question(
                "PY-INT-02", JobRole.PYTHON_DEVELOPER, DifficultyLevel.INTERMEDIATE, "Concurrency & Async",
                "Explain the Global Interpreter Lock (GIL) in CPython and how it impacts multithreading vs multiprocessing.",
                List.of("gil", "cpython", "mutex", "cpu-bound", "i/o-bound", "multiprocessing", "threading"),
                List.of("gil prevents multiple native threads from executing python bytecode simultaneously", "multithreading benefits i/o-bound tasks", "multiprocessing is required for parallel cpu-bound tasks"),
                "The GIL is a mutex in CPython that ensures only one thread executes Python bytecode at a time, simplifying memory management. Consequently, standard multithreading cannot achieve true parallelism for CPU-bound tasks in Python; developers must use multiprocessing or async I/O instead.",
                "How does Python's asyncio differ fundamentally from multi-threading?",
                List.of("event loop", "cooperative", "coroutine", "single-threaded", "non-blocking")
        ));

        add(new Question(
                "PY-INT-03", JobRole.PYTHON_DEVELOPER, DifficultyLevel.INTERMEDIATE, "Web Frameworks",
                "Compare FastAPI and Django. In what scenarios would you choose FastAPI over Django?",
                List.of("fastapi", "django", "pydantic", "orm", "async", "type hints", "batteries included"),
                List.of("django is full-stack with orm, admin, auth batteries included", "fastapi is modern, lightweight, async-first with automatic swagger and pydantic validation", "fastapi excels for high-performance microservices and rest apis"),
                "Django is an all-inclusive monolithic framework with built-in ORM, admin panel, auth, and template engine. FastAPI is a modern, high-performance, asynchronous micro-framework built on Starlette and Pydantic, offering native type hints, automatic OpenAPI docs, and async performance ideal for APIs and microservices.",
                "How does FastAPI use Pydantic for request validation and serialization?",
                List.of("pydantic", "basemodel", "schema", "validation", "serialization")
        ));

        add(new Question(
                "PY-INT-04", JobRole.PYTHON_DEVELOPER, DifficultyLevel.INTERMEDIATE, "Object Oriented Python",
                "What is the difference between @staticmethod, @classmethod, and regular instance methods in Python classes?",
                List.of("staticmethod", "classmethod", "self", "cls", "instance", "factory"),
                List.of("instance methods receive self and access instance state", "classmethod receives cls and accesses class state, useful as factory constructors", "staticmethod receives neither self nor cls and acts as a pure utility"),
                "Regular instance methods take 'self' as their first argument to access instance state. Class methods take 'cls' using @classmethod to interact with class-level attributes or act as alternate factory constructors. Static methods using @staticmethod take neither 'self' nor 'cls' and serve as namespaced utility functions.",
                "Provide an example of using a @classmethod as an alternative constructor.",
                List.of("constructor", "from_dict", "from_json", "factory", "cls")
        ));

        add(new Question(
                "PY-INT-05", JobRole.PYTHON_DEVELOPER, DifficultyLevel.INTERMEDIATE, "Packaging & Testing",
                "Explain how pytest fixtures and parameterization work, and how they promote clean, maintainable unit test suites.",
                List.of("pytest", "fixture", "parametrize", "dependency injection", "setup", "teardown", "scope"),
                List.of("fixtures provide reusable test setup and teardown via yield", "scope controls lifecycle (function, module, session)", "@pytest.mark.parametrize runs test with multiple input/expected combinations"),
                "Pytest fixtures use dependency injection to prepare resources (like db connections or mocks) and clean them up after tests using yield statements. Fixture scopes (function, module, session) prevent repeated setup. @pytest.mark.parametrize allows running identical test assertions against diverse datasets cleanly.",
                "How does fixture teardown differ when using a 'yield' statement instead of a try/finally block?",
                List.of("yield", "teardown", "cleanup", "execution", "lifecycle")
        ));

        // Advanced (5)
        add(new Question(
                "PY-ADV-01", JobRole.PYTHON_DEVELOPER, DifficultyLevel.ADVANCED, "Internals & Metaprogramming",
                "Explain Python Metaclasses, how the type-class hierarchy operates, and when you would use '__init_subclass__' instead.",
                List.of("metaclass", "type", "__init_subclass__", "class creation", "__new__"),
                List.of("metaclasses define how classes themselves are constructed", "type is the default metaclass", "__init_subclass__ provides a simpler hook to customize subclasses without full metaclasses"),
                "In Python, classes are themselves instances of metaclasses, with 'type' being the default metaclass. Metaclasses intercept class definition to validate, modify, or register new classes via __new__ and __init__. Since Python 3.6, '__init_subclass__' is preferred for most subclass hook requirements due to simpler syntax and less inheritance friction.",
                "How would you enforce that every subclass must implement a specific class attribute using __init_subclass__?",
                List.of("__init_subclass__", "attribute", "validation", "typeerror", "hook")
        ));

        add(new Question(
                "PY-ADV-02", JobRole.PYTHON_DEVELOPER, DifficultyLevel.ADVANCED, "Async Internals",
                "How does the Python asyncio Event Loop schedule coroutines, tasks, and callbacks under the hood, and what causes event loop blocking?",
                List.of("event loop", "coroutine", "task", "future", "selectors", "blocking", "run_in_executor"),
                List.of("event loop uses os selectors (epoll/kqueue) to multiplex i/o non-blockingly", "coroutines are wrapped in tasks", "synchronous cpu work or blocking syscalls starve the loop"),
                "The asyncio event loop uses OS primitives (epoll/kqueue) via the selectors module to poll registered sockets. Tasks wrap coroutines into Futures and schedule them on loop iterations. Any synchronous CPU computation or blocking I/O (e.g. time.sleep, requests.get) blocks the entire single-threaded loop; such work must be offloaded via loop.run_in_executor.",
                "How does asyncio.gather differ from TaskGroup introduced in Python 3.11?",
                List.of("taskgroup", "gather", "structured concurrency", "exception handling", "cleanup")
        ));

        add(new Question(
                "PY-ADV-03", JobRole.PYTHON_DEVELOPER, DifficultyLevel.ADVANCED, "Memory Management & Profiling",
                "Explain CPython's cyclic garbage collection, reference counting, and how __del__ and circular references can cause memory leaks.",
                List.of("reference counting", "cyclic gc", "gc module", "weakref", "circular reference", "leak"),
                List.of("cpython immediately deallocates objects when reference count reaches zero", "cyclic generational gc detects isolated reference cycles", "weakref prevents strong reference cycles"),
                "CPython primarily relies on reference counting for deterministic deallocation when an object's count hits zero. To resolve circular references where counts never hit zero, a generational cyclic GC runs periodically. Unmanaged cycles or objects with custom finalizers can impede GC cleanup; using 'weakref' avoids cycle creation altogether.",
                "How can you profile memory allocation hotspots in a live production Python service?",
                List.of("tracemalloc", "memory_profiler", "guppy", "objgraph", "snapshot")
        ));

        add(new Question(
                "PY-ADV-04", JobRole.PYTHON_DEVELOPER, DifficultyLevel.ADVANCED, "High Performance Python",
                "How would you optimize a CPU-bound data transformation pipeline in Python without rewriting it completely in C++?",
                List.of("numba", "cython", "vectorization", "numpy", "multiprocessing", "pypy"),
                List.of("vectorize calculations using numpy/polars", "jit compile numeric loops with numba", "use multiprocessing to bypass the gil across cpu cores"),
                "Optimization options include vectorizing operations using NumPy or Polars to leverage SIMD in C, using Numba JIT compilation to compile Python functions to machine code at runtime, offloading tasks across CPU cores using concurrent.futures.ProcessPoolExecutor, or rewriting bottlenecks in Cython or Rust (via PyO3).",
                "What is the difference between SIMD vectorization and multi-core parallelism?",
                List.of("simd", "cpu instructions", "parallelism", "cores", "threads")
        ));

        add(new Question(
                "PY-ADV-05", JobRole.PYTHON_DEVELOPER, DifficultyLevel.ADVANCED, "Design & Architecture",
                "How would you design a scalable background task queue system in Python similar to Celery using Redis/RabbitMQ?",
                List.of("celery", "task queue", "redis", "rabbitmq", "worker", "ack", "idempotency"),
                List.of("producers push serialized messages to broker queue", "workers consume and process messages with acknowledgements", "dead letter queues and idempotent task execution"),
                "A scalable queue architecture consists of a message broker (RabbitMQ or Redis) where producers enqueue serialized task payloads. Worker processes consume tasks, acknowledge messages upon successful execution, push failed jobs to Dead-Letter Queues (DLQ) with exponential backoff retries, and ensure idempotency via unique task IDs.",
                "How do you handle worker crashes mid-task to guarantee at-least-once message processing without duplicate state corruption?",
                List.of("acknowledgement", "ack", "idempotency", "visibility timeout", "redelivery")
        ));

        // =========================================================================
        // 3. SOFTWARE ENGINEER
        // =========================================================================
        // Beginner (5)
        add(new Question(
                "SE-BEG-01", JobRole.SOFTWARE_ENGINEER, DifficultyLevel.BEGINNER, "Data Structures & Algorithms",
                "Explain the concept of Big O notation and give the time complexities for searching an element in an unsorted array vs a sorted array.",
                List.of("big o", "time complexity", "linear", "binary search", "o(n)", "o(log n)"),
                List.of("big o describes how runtime grows as input size increases", "unsorted array search is o(n) linear time", "sorted array search using binary search is o(log n) logarithmic time"),
                "Big O notation characterizes the asymptotic upper bound of an algorithm's growth rate in relation to input size n. Searching an unsorted array requires scanning elements one-by-one resulting in O(n) linear time, whereas a sorted array can use Binary Search to halve the search space at each step resulting in O(log n) logarithmic time.",
                "What is the space complexity of an iterative binary search compared to a recursive binary search?",
                List.of("space complexity", "call stack", "o(1)", "o(log n)", "iterative")
        ));

        add(new Question(
                "SE-BEG-02", JobRole.SOFTWARE_ENGINEER, DifficultyLevel.BEGINNER, "Software Principles",
                "What do the letters in the SOLID principles stand for, and why are they fundamental in software development?",
                List.of("solid", "single responsibility", "open closed", "liskov", "interface segregation", "dependency inversion"),
                List.of("s: single responsibility", "o: open/closed", "l: liskov substitution", "i: interface segregation", "d: dependency inversion", "improves maintainability, extensibility, and testability"),
                "SOLID stands for Single Responsibility, Open-Closed, Liskov Substitution, Interface Segregation, and Dependency Inversion. These five design principles guide engineers to build modular, maintainable, loosely-coupled, and easily extensible software systems that resist regression bugs.",
                "Can you explain the Single Responsibility Principle with a quick code violation example?",
                List.of("single responsibility", "one reason to change", "violation", "cohesion")
        ));

        add(new Question(
                "SE-BEG-03", JobRole.SOFTWARE_ENGINEER, DifficultyLevel.BEGINNER, "Version Control & Git",
                "Explain the difference between 'git merge' and 'git rebase'. When should each be used?",
                List.of("git merge", "git rebase", "commit history", "linear", "merge commit", "fast-forward"),
                List.of("merge preserves full historical context with a merge commit", "rebase rewrites commits onto new base for a linear history", "never rebase shared public branches"),
                "Git merge combines two branch histories by creating a 3-way merge commit, preserving exact chronological history. Git rebase moves or replays branch commits on top of the target base commit, creating a clean linear history. The golden rule is never rebase shared public branches because it rewrites commit SHAs.",
                "What are the dangers of force-pushing (git push --force) after a rebase on a shared branch?",
                List.of("force push", "overwrite", "conflict", "collaborators", "lost commits")
        ));

        add(new Question(
                "SE-BEG-04", JobRole.SOFTWARE_ENGINEER, DifficultyLevel.BEGINNER, "Database Basics",
                "What are the ACID properties in database management systems, and why do they matter?",
                List.of("acid", "atomicity", "consistency", "isolation", "durability", "transaction"),
                List.of("atomicity ensures all or nothing", "consistency ensures valid state transitions", "isolation prevents concurrent transaction interference", "durability guarantees committed data survives crashes"),
                "ACID guarantees reliable transaction processing in relational databases: Atomicity (all operations succeed or all rollback), Consistency (data conforms to all constraints before and after), Isolation (concurrent transactions execute independently without dirty reads), and Durability (committed modifications persist despite power or system failures).",
                "Which ACID property is compromised if a dirty read occurs?",
                List.of("isolation", "dirty read", "read uncommitted", "concurrency")
        ));

        add(new Question(
                "SE-BEG-05", JobRole.SOFTWARE_ENGINEER, DifficultyLevel.BEGINNER, "Web & Networking",
                "What happens when you type a URL into a web browser and press Enter until the page renders?",
                List.of("dns", "ip address", "tcp", "handshake", "http", "tls", "render", "dom"),
                List.of("dns lookup resolves domain to ip", "tcp 3-way handshake and tls handshake", "http get request and server response", "browser parses html/css/js to construct dom and render tree"),
                "The browser resolves the domain to an IP address via DNS cache/resolvers. Next, a TCP 3-way handshake (and TLS handshake for HTTPS) establishes a connection. The browser sends an HTTP GET request, the server responds with status and HTML/CSS/assets, and the browser parses the DOM and CSSOM to compute layout and paint pixels to the screen.",
                "What is the role of the browser cache in speeding up subsequent requests for the same URL?",
                List.of("cache", "etags", "cache-control", "304", "expires")
        ));

        // Intermediate (5)
        add(new Question(
                "SE-INT-01", JobRole.SOFTWARE_ENGINEER, DifficultyLevel.INTERMEDIATE, "System Design & Architecture",
                "Explain the CAP theorem and the trade-offs between Consistency, Availability, and Partition Tolerance in distributed databases.",
                List.of("cap theorem", "consistency", "availability", "partition tolerance", "network partition", "cp", "ap"),
                List.of("in a distributed network partition, you must choose consistency or availability", "cp systems favor consistency by rejecting writes during partitions", "ap systems favor availability by serving possibly stale data"),
                "The CAP theorem states that in a distributed data store experiencing a network partition (P), the system can guarantee either Consistency (C - every read receives the latest write or an error) or Availability (A - every non-failing node returns a response), but cannot provide both simultaneously.",
                "Give an example of a CP system and an AP system, and justify when you'd choose each.",
                List.of("hbase", "cassandra", "mongodb", "dynamodb", "trade-off")
        ));

        add(new Question(
                "SE-INT-02", JobRole.SOFTWARE_ENGINEER, DifficultyLevel.INTERMEDIATE, "API Design & Microservices",
                "What are the best practices for designing a secure, scalable, and versioned RESTful API?",
                List.of("rest", "idempotency", "versioning", "status codes", "pagination", "rate limiting", "jwt"),
                List.of("use proper http verbs and status codes", "implement uri or header versioning", "support pagination, filtering, and rate limiting", "stateless token authentication like oauth/jwt"),
                "RESTful API best practices include: semantic use of HTTP methods (GET, POST, PUT, DELETE) and status codes (200, 201, 400, 404, 500); clear URI resource hierarchy; explicit API versioning (e.g., /api/v1/); cursor-based pagination for collections; rate limiting to prevent abuse; and stateless authentication via JWT or OAuth2.",
                "Why is cursor-based pagination usually preferred over offset/limit pagination for large datasets?",
                List.of("cursor", "offset", "performance", "deep pagination", "index")
        ));

        add(new Question(
                "SE-INT-03", JobRole.SOFTWARE_ENGINEER, DifficultyLevel.INTERMEDIATE, "Database & Caching",
                "Explain the Cache-Aside, Write-Through, and Write-Behind caching strategies with Redis. What are the trade-offs of each?",
                List.of("cache-aside", "write-through", "write-behind", "redis", "invalidation", "staleness", "latency"),
                List.of("cache-aside: application queries cache then db on miss", "write-through: writes update cache and db synchronously", "write-behind: writes update cache immediately and db asynchronously"),
                "In Cache-Aside, the application reads from cache and on a miss fetches from DB and populates cache. In Write-Through, writes update cache and DB synchronously, guaranteeing consistency at the cost of write latency. In Write-Behind (write-back), writes update cache immediately and asynchronously persist to DB, optimizing write speed but risking data loss if the cache crashes.",
                "How do you prevent a 'cache stampede' or 'thundering herd' problem when a popular key expires?",
                List.of("cache stampede", "mutex", "probabilistic early expiration", "pre-warming", "lock")
        ));

        add(new Question(
                "SE-INT-04", JobRole.SOFTWARE_ENGINEER, DifficultyLevel.INTERMEDIATE, "Concurrency & Locking",
                "Explain the difference between Optimistic Locking and Pessimistic Locking. When would you choose one over the other?",
                List.of("optimistic locking", "pessimistic locking", "version", "select for update", "contention", "deadlock"),
                List.of("optimistic uses version/timestamp check on commit, best for low contention", "pessimistic acquires db row lock upfront, best for high contention", "tradeoff between throughput and conflict overhead"),
                "Optimistic locking assumes conflicts are rare; it checks a version or timestamp column upon commit and fails or retries if concurrent updates occurred, providing high throughput. Pessimistic locking assumes frequent conflicts and locks records upfront (e.g. SELECT FOR UPDATE), guaranteeing safety at the expense of concurrency and higher deadlock risk.",
                "How does an application gracefully handle an OptimisticLockException?",
                List.of("retry", "backoff", "re-fetch", "conflict", "idempotent")
        ));

        add(new Question(
                "SE-INT-05", JobRole.SOFTWARE_ENGINEER, DifficultyLevel.INTERMEDIATE, "DevOps & CI/CD",
                "What is the difference between Continuous Integration, Continuous Delivery, and Continuous Deployment?",
                List.of("ci", "cd", "continuous integration", "continuous delivery", "continuous deployment", "pipeline", "automated testing"),
                List.of("ci automatically builds and tests code commits", "continuous delivery automates release preparation requiring manual approval to deploy", "continuous deployment automatically deploys every green build directly to production"),
                "Continuous Integration (CI) automatically builds and runs automated test suites whenever code is committed to catch bugs early. Continuous Delivery (CD) automates the pipeline so software is always in a deployable state, with a manual trigger for production rollout. Continuous Deployment goes a step further by deploying every passing build directly to production automatically without human intervention.",
                "What role do canary deployments or blue-green deployments play in Continuous Deployment?",
                List.of("canary", "blue green", "zero downtime", "rollback", "traffic routing")
        ));

        // Advanced (5)
        add(new Question(
                "SE-ADV-01", JobRole.SOFTWARE_ENGINEER, DifficultyLevel.ADVANCED, "Distributed Consensus",
                "Explain how consensus algorithms like Raft or Paxos work, and how leader election and log replication prevent split-brain scenarios.",
                List.of("raft", "paxos", "consensus", "leader election", "log replication", "quorum", "split brain", "heartbeat"),
                List.of("nodes vote for leader requiring majority quorum", "leader commits log entries only after majority replication", "term numbers and quorums prevent split-brain"),
                "Consensus algorithms like Raft achieve fault tolerance via a single elected leader. Raft divides state into Leader, Follower, and Candidate. Leader election requires a majority quorum (N/2 + 1) within randomized election timeouts. Log replication ensures entries are only committed after surviving on a majority of nodes. Strict quorum requirements prevent two partitions from independently electing leaders, eliminating split-brain.",
                "What happens in Raft when a network partition isolates the leader with a minority of nodes while the remaining majority elects a new leader?",
                List.of("partition", "term", "step down", "uncommitted", "reconciliation")
        ));

        add(new Question(
                "SE-ADV-02", JobRole.SOFTWARE_ENGINEER, DifficultyLevel.ADVANCED, "System Design at Scale",
                "How would you design a globally distributed URL shortener (like TinyURL) handling 10 billion URLs with sub-50ms latency?",
                List.of("base62", "hashing", "key generation service", "caching", "replication", "cdn", "sharding"),
                List.of("use base62 encoding with pre-generated range allocations", "cache hot urls in redis or memcached", "shard database using consistent hashing across geo-distributed replicas"),
                "Architecture: A Key Generation Service (KGS) pre-generates unique 7-character Base62 keys in batches to avoid runtime collisions. Reads hit a geo-distributed CDN and distributed Redis caches (90%+ hit rate for hot links). Backing storage uses a horizontally sharded NoSQL database (like DynamoDB/Cassandra) partitioned by key hash with multi-region active-active replication to maintain sub-50ms global latency.",
                "How do you handle key collision or coordinate key ranges among multiple distributed KGS instances?",
                List.of("range allocation", "zookeeper", "etcd", "offset", "coordination")
        ));

        add(new Question(
                "SE-ADV-03", JobRole.SOFTWARE_ENGINEER, DifficultyLevel.ADVANCED, "Database Architecture",
                "Explain the architecture of Log-Structured Merge (LSM) Trees compared to B+ Trees, and explain why write-heavy systems prefer LSM Trees.",
                List.of("lsm tree", "b+ tree", "memtable", "sstable", "compaction", "write amplification", "sequential write"),
                List.of("b+ trees update disk pages in place causing random i/o", "lsm trees append writes sequentially to memtable and sstables", "compaction merges sstables in background"),
                "B+ Trees organize data in balanced tree pages, requiring random in-place disk writes which create I/O bottlenecks. LSM Trees (used in Cassandra, RocksDB) write all mutations sequentially to an in-memory Memtable and Write-Ahead Log (WAL), flushing immutable Sorted String Tables (SSTables) to disk. Background compaction merges SSTables, converting expensive random writes into fast sequential disk I/O.",
                "What is the impact of LSM compaction on read amplification and disk I/O?",
                List.of("read amplification", "bloom filters", "compaction", "disk i/o", "latency")
        ));

        add(new Question(
                "SE-ADV-04", JobRole.SOFTWARE_ENGINEER, DifficultyLevel.ADVANCED, "Observability & Site Reliability",
                "What is Distributed Tracing, how do trace contexts propagate across asynchronous microservices, and how do you calculate tail latency (p99)?",
                List.of("distributed tracing", "trace id", "span id", "opentelemetry", "w3c tracecontext", "tail latency", "p99"),
                List.of("propagates trace and span ids via http headers and message metadata", "collects spans into directed acyclic graphs of requests", "p99 measures the latency experienced by the worst 1% of requests"),
                "Distributed tracing (e.g. OpenTelemetry) tracks requests across distributed boundaries. A unique TraceId and hierarchical SpanIds are propagated through HTTP headers (W3C Trace Context) or message metadata. A collector aggregates spans into a timeline. Tail latency (p99/p99.9) captures the slowest 1% or 0.1% of requests using percentile algorithms (like T-Digest), which reveal concurrency bottlenecks hidden by average latency.",
                "Why is averaging latency (mean) deceptive when monitoring microservice performance?",
                List.of("mean", "outlier", "skew", "percentile", "user experience")
        ));

        add(new Question(
                "SE-ADV-05", JobRole.SOFTWARE_ENGINEER, DifficultyLevel.ADVANCED, "Security Architecture",
                "Explain the OAuth 2.0 Authorization Code Flow with PKCE (Proof Key for Code Exchange) and why PKCE is required for Single Page Applications (SPAs).",
                List.of("oauth 2.0", "pkce", "code verifier", "code challenge", "spa", "authorization code", "client secret"),
                List.of("spas cannot securely store client secrets", "pkce generates code_verifier and code_challenge sha256 hash", "prevents authorization code interception attacks"),
                "In standard OAuth 2.0, the Authorization Code flow uses a client secret. Because SPAs and mobile apps are public clients and cannot keep secrets safe from client-side inspection, PKCE replaces the client secret. The client generates a random 'code_verifier' and transforms it into a 'code_challenge'. The authorization server records the challenge and only issues tokens if the client provides the matching verifier during token exchange, mitigating code interception.",
                "How does the authorization server verify the code_verifier matches the code_challenge?",
                List.of("sha256", "hash", "verify", "match", "crypto")
        ));

        // =========================================================================
        // 4. DATA ANALYST
        // =========================================================================
        // Beginner (5)
        add(new Question(
                "DA-BEG-01", JobRole.DATA_ANALYST, DifficultyLevel.BEGINNER, "SQL Fundamentals",
                "Explain the difference between INNER JOIN, LEFT JOIN, RIGHT JOIN, and FULL OUTER JOIN in SQL with diagrams or clear examples.",
                List.of("inner join", "left join", "right join", "outer join", "null", "matching records"),
                List.of("inner join returns only matching rows from both tables", "left join returns all left rows plus matching right rows with nulls", "full outer join returns all rows from both tables"),
                "INNER JOIN returns only rows that have matching values in both tables. LEFT JOIN returns all rows from the left table and matched rows from the right (with NULLs for unmatched right records). RIGHT JOIN is the converse. FULL OUTER JOIN combines results, returning all rows from both tables with NULLs wherever no match exists.",
                "What is the result if a LEFT JOIN produces multiple matching rows in the right table for a single left row?",
                List.of("duplicate", "multiplication", "cartesian", "fan out", "multiple rows")
        ));

        add(new Question(
                "DA-BEG-02", JobRole.DATA_ANALYST, DifficultyLevel.BEGINNER, "SQL Aggregation",
                "What is the difference between the WHERE clause and the HAVING clause in a SQL query?",
                List.of("where", "having", "group by", "aggregation", "filter", "aggregate functions"),
                List.of("where filters individual rows before grouping and aggregation", "having filters groups after aggregation", "where cannot use aggregate functions like count or sum"),
                "The WHERE clause filters individual rows before any grouping or aggregation takes place, and cannot evaluate aggregate functions (such as SUM, COUNT, AVG). The HAVING clause filters grouped summary records after the GROUP BY aggregation has executed.",
                "Can a SQL query include both a WHERE clause and a HAVING clause simultaneously?",
                List.of("both", "pre-filter", "post-filter", "order of execution", "valid")
        ));

        add(new Question(
                "DA-BEG-03", JobRole.DATA_ANALYST, DifficultyLevel.BEGINNER, "Statistics Basics",
                "Explain Mean, Median, and Mode. In what business scenario would you prefer using the Median over the Mean?",
                List.of("mean", "median", "mode", "outliers", "skewed", "average", "distribution"),
                List.of("mean is arithmetic average", "median is middle value in sorted set", "mode is most frequent value", "median is resilient against extreme outliers in skewed data like salaries"),
                "Mean is the arithmetic average of numbers. Median is the middle value when sorted. Mode is the most frequently occurring value. The Median is strongly preferred when dealing with skewed distributions containing extreme outliers—such as household incomes, house prices, or user session lengths—where a few outliers would artificially distort the mean.",
                "If a distribution is right-skewed (positively skewed), will the mean typically be higher or lower than the median?",
                List.of("mean higher", "right skew", "pull", "outlier", "tail")
        ));

        add(new Question(
                "DA-BEG-04", JobRole.DATA_ANALYST, DifficultyLevel.BEGINNER, "Data Cleaning",
                "What are the most common techniques to handle missing data (NULLs or NaNs) in a dataset?",
                List.of("missing data", "imputation", "deletion", "mean", "median", "drop", "indicator"),
                List.of("listwise deletion or dropping null rows", "statistical imputation using mean, median, or mode", "predictive imputation or domain-specific default flags"),
                "Strategies include: 1) Deletion (dropping rows/columns with excessive missingness, suitable if missing completely at random); 2) Imputation (replacing missing values with median for numeric or mode for categorical features); 3) Model-based imputation (KNN, regression); and 4) Adding a missingness indicator flag to preserve signal.",
                "When is dropping rows with missing values dangerous for an analytical model?",
                List.of("bias", "sample size", "non-random", "not at random", "distortion")
        ));

        add(new Question(
                "DA-BEG-05", JobRole.DATA_ANALYST, DifficultyLevel.BEGINNER, "Data Visualization",
                "When would you choose a Bar Chart versus a Line Chart versus a Scatter Plot for business reporting?",
                List.of("bar chart", "line chart", "scatter plot", "time series", "categories", "correlation", "trend"),
                List.of("bar charts compare discrete categories", "line charts track continuous trends over time", "scatter plots visualize relationships and correlations between two numeric variables"),
                "Bar charts are best for comparing discrete categories or rankings. Line charts are optimal for displaying continuous trends and time-series patterns over time. Scatter plots are essential for investigating correlations, clusters, and bivariate relationships between two continuous numeric variables.",
                "Why should you avoid using 3D chart effects in business presentations?",
                List.of("distortion", "readability", "misleading", "perspective", "clarity")
        ));

        // Intermediate (5)
        add(new Question(
                "DA-INT-01", JobRole.DATA_ANALYST, DifficultyLevel.INTERMEDIATE, "Advanced SQL",
                "Explain SQL Window Functions. What is the difference between ROW_NUMBER(), RANK(), and DENSE_RANK()?",
                List.of("window function", "over", "partition by", "row_number", "rank", "dense_rank", "ties"),
                List.of("window functions calculate across row sets without collapsing rows", "row_number assigns unique sequential integers", "rank leaves gaps for ties", "dense_rank has no gaps for ties"),
                "Window functions perform calculations across a set of rows related to the current row without collapsing the result set. ROW_NUMBER() assigns a unique sequential integer to each row. RANK() assigns the same rank to ties and skips subsequent numbers (e.g., 1, 2, 2, 4). DENSE_RANK() assigns the same rank to ties but does not skip any ranks (e.g., 1, 2, 2, 3).",
                "How would you use a window function to find the running cumulative sum of sales by month?",
                List.of("sum", "over", "order by", "cumulative", "rows between")
        ));

        add(new Question(
                "DA-INT-02", JobRole.DATA_ANALYST, DifficultyLevel.INTERMEDIATE, "Python & Pandas",
                "How do you perform data aggregation and reshaping in Pandas using 'groupby', 'pivot_table', and 'melt'?",
                List.of("pandas", "groupby", "pivot_table", "melt", "wide to long", "aggregation", "dataframe"),
                List.of("groupby splits data into groups and applies aggregate functions", "pivot_table reshapes data from long to wide with aggregation", "melt unpivots data from wide to long format"),
                "Pandas 'groupby' splits a DataFrame on specified columns, applies aggregate functions (mean, count, sum), and combines the results. 'pivot_table' summarizes and reshapes data from tall/tidy format to wide multidimensional format. 'melt' performs the reverse transformation, unpivoting wide columns into key-value pairs.",
                "What is the difference between 'merge' and 'concat' in Pandas?",
                List.of("merge", "concat", "join", "axis", "keys")
        ));

        add(new Question(
                "DA-INT-03", JobRole.DATA_ANALYST, DifficultyLevel.INTERMEDIATE, "A/B Testing & Hypothesis Testing",
                "How do you design an A/B test for a product feature, calculate sample size, and interpret the p-value?",
                List.of("a/b test", "hypothesis", "p-value", "statistical significance", "sample size", "null hypothesis", "type i error"),
                List.of("formulate null and alternative hypotheses", "determine sample size based on power, mde, and significance level", "p-value under alpha (0.05) rejects null hypothesis"),
                "An A/B test establishes a null hypothesis (no effect) and alternative hypothesis. Minimum sample size is calculated using statistical power (typically 80%), significance level (alpha = 0.05), and Minimum Detectable Effect (MDE). The p-value indicates the probability of observing results as extreme assuming the null hypothesis is true; if p < 0.05, we reject the null with statistical significance.",
                "What is the difference between a Type I error (false positive) and a Type II error (false negative)?",
                List.of("type i", "type ii", "false positive", "false negative", "alpha", "beta")
        ));

        add(new Question(
                "DA-INT-04", JobRole.DATA_ANALYST, DifficultyLevel.INTERMEDIATE, "Data Modeling & Warehousing",
                "Compare the Star Schema and Snowflake Schema in dimensional data modeling. When should you use each?",
                List.of("star schema", "snowflake schema", "fact table", "dimension table", "normalization", "denormalization"),
                List.of("star schema denormalizes dimensions into a single level surrounding fact tables", "snowflake schema normalizes dimensions into sub-dimension tables", "star schema provides simpler queries and faster bi performance"),
                "In a Star Schema, fact tables connect directly to denormalized dimension tables, optimizing for query simplicity, fewer joins, and fast analytics engine execution. A Snowflake Schema normalizes dimension tables into hierarchies (e.g. product -> subcategory -> category), reducing data redundancy but increasing join complexity and query latency.",
                "What is a Slowly Changing Dimension (SCD Type 2) and how does it preserve history?",
                List.of("scd type 2", "surrogate key", "effective date", "history", "versioning")
        ));

        add(new Question(
                "DA-INT-05", JobRole.DATA_ANALYST, DifficultyLevel.INTERMEDIATE, "Business Metrics & Analytics",
                "Explain the Customer Lifetime Value (LTV) and Customer Acquisition Cost (CAC) ratio, and why cohort analysis is critical.",
                List.of("ltv", "cac", "cohort analysis", "churn", "retention", "payback period"),
                List.of("ltv measures total revenue expected from customer", "cac measures marketing/sales cost to acquire customer", "ideal ltv:cac ratio is 3:1 or higher", "cohort analysis tracks retention and behavior of user groups over time"),
                "CAC measures the total sales and marketing cost divided by new customers acquired. LTV calculates the total margin contributed by a customer over their entire lifecycle. A healthy SaaS benchmark is an LTV:CAC ratio of 3:1 with payback under 12 months. Cohort analysis tracks customer groups defined by acquisition month to spot trends in churn, retention, and repeat purchases over time.",
                "How does high customer churn rate undermine a seemingly healthy CAC?",
                List.of("churn", "ltv", "retention", "burn", "unprofitable")
        ));

        // Advanced (5)
        add(new Question(
                "DA-ADV-01", JobRole.DATA_ANALYST, DifficultyLevel.ADVANCED, "Causal Inference & Experimentation",
                "When A/B testing is not feasible due to network effects or ethical constraints, what quasi-experimental methods can establish causality?",
                List.of("difference in differences", "propensity score matching", "synthetic control", "instrumental variables", "causality"),
                List.of("difference in differences compares pre/post changes between treatment and control", "propensity score matching pairs similar units to control confounders", "synthetic control constructs a weighted counterfactual unit"),
                "When randomized controlled trials cannot be run, quasi-experimental methods are used: 1) Difference-in-Differences (DiD) compares trends before and after intervention assuming parallel trends; 2) Propensity Score Matching (PSM) balances observed covariates between treated and untreated groups; 3) Synthetic Control constructs a weighted combination of control units to simulate the counterfactual; 4) Regression Discontinuity Design (RDD) exploits cutoff thresholds.",
                "What is the critical 'parallel trends assumption' in Difference-in-Differences analysis?",
                List.of("parallel trends", "counterfactual", "pre-treatment", "assumption", "bias")
        ));

        add(new Question(
                "DA-ADV-02", JobRole.DATA_ANALYST, DifficultyLevel.ADVANCED, "Advanced SQL & Performance Tuning",
                "How do SQL query engines optimize execution plans, and how do you diagnose and fix query performance bottlenecks involving billions of rows?",
                List.of("explain plan", "indexes", "partitioning", "broadcast join", "hash join", "shuffling", "spill to disk"),
                List.of("inspect query execution plan using explain analyze", "add composite indexes or partition pruning to reduce scanned data", "tune join algorithms and minimize data shuffling across nodes"),
                "Diagnosis begins by running EXPLAIN ANALYZE to inspect costs, node operations, and row estimates. Common issues include full table scans (resolved with indexes or partition pruning), skewed distributions causing disk spills, and expensive nested loop joins on large tables. In distributed engines (BigQuery/Snowflake/Spark), optimization focuses on partition pruning, clustering, broadcast joins for small lookup tables, and eliminating data shuffling.",
                "What is the difference between a Hash Join and a Merge Join in relational database engines?",
                List.of("hash join", "merge join", "sorted", "in-memory", "cost")
        ));

        add(new Question(
                "DA-ADV-03", JobRole.DATA_ANALYST, DifficultyLevel.ADVANCED, "Predictive Analytics & Forecasting",
                "How do you evaluate time-series forecasting models (like ARIMA, Prophet, or XGBoost), and how do you avoid lookahead bias?",
                List.of("time series", "arima", "prophet", "mape", "rmse", "walk-forward validation", "lookahead bias"),
                List.of("use walk-forward or expanding window time-series cross-validation", "never use future data in feature engineering or scaling", "evaluate accuracy using mape, mae, and rmse"),
                "Time series models must be evaluated using temporal cross-validation (walk-forward or rolling window validation) where training data strictly precedes test data. Standard random k-fold cross-validation causes fatal lookahead bias. Accuracy is measured using MAPE (Mean Absolute Percentage Error), RMSE, or MAE, while residual autocorrelation is checked with the Ljung-Box test to ensure no predictive signal remains uncaptured.",
                "Why is R-squared generally inappropriate for non-stationary time series regression?",
                List.of("non-stationary", "spurious correlation", "trend", "differencing")
        ));

        add(new Question(
                "DA-ADV-04", JobRole.DATA_ANALYST, DifficultyLevel.ADVANCED, "Data Governance & Data Quality",
                "How would you architect an automated Data Quality Framework for a critical enterprise data lake to ensure data freshness, completeness, and schema consistency?",
                List.of("data quality", "great expectations", "data contract", "freshness", "schema drift", "anomaly detection", "lineage"),
                List.of("implement automated test assertions for nulls, ranges, and uniqueness", "enforce schema contracts at ingestion boundaries", "monitor data freshness and volume anomalies using automated alerting"),
                "An enterprise data quality framework implements automated testing at ingestion and transformation layers (using tools like Great Expectations or dbt tests). It verifies: 1) Freshness (SLA tracking); 2) Volume anomalies (z-score checks on row counts); 3) Schema validation (enforcing data contracts to prevent drift); 4) Uniqueness and foreign key integrity; and 5) Automated lineage tracking to quarantine bad partitions and alert downstream stakeholders before corrupted metrics hit executive dashboards.",
                "How do you prevent bad data from polluting downstream dashboards without stopping the entire daily ingestion pipeline?",
                List.of("quarantine", "dead letter", "circuit breaker", "alerting", "isolation")
        ));

        add(new Question(
                "DA-ADV-05", JobRole.DATA_ANALYST, DifficultyLevel.ADVANCED, "Marketing Analytics & Attribution",
                "Compare multi-touch attribution models (First-Touch, Last-Touch, Linear, Time-Decay, and Markov Chain / Shapley Value) for omni-channel marketing.",
                List.of("attribution", "multi-touch", "last-touch", "markov chain", "shapley value", "touchpoints", "roas"),
                List.of("single-touch models (first/last) oversimplify customer journeys", "rule-based models (linear, time-decay) use arbitrary weights", "algorithmic models (markov, shapley) calculate true marginal incremental contribution"),
                "Single-touch models attribute 100% credit to either the discovery touchpoint (First-Touch) or conversion touchpoint (Last-Touch), failing to account for multi-channel journeys. Rule-based models (Linear, Time-Decay, U-Shaped) distribute credit using subjective heuristics. Algorithmic attribution using Markov Chains (removal effect) or Shapley Values mathematically quantifies the true marginal incremental contribution of each marketing channel, enabling optimized marketing budget allocation.",
                "How does the Markov Chain removal effect determine the attribution weight of an advertising channel?",
                List.of("removal effect", "conversion probability", "baseline", "contribution", "transition matrix")
        ));

        // =========================================================================
        // 5. WEB DEVELOPER
        // =========================================================================
        // Beginner (5)
        add(new Question(
                "WEB-BEG-01", JobRole.WEB_DEVELOPER, DifficultyLevel.BEGINNER, "HTML & CSS",
                "Explain the CSS Box Model and the difference between 'content-box' and 'border-box' sizing.",
                List.of("box model", "content", "padding", "border", "margin", "box-sizing", "border-box"),
                List.of("box model consists of content, padding, border, and margin", "content-box calculates width purely from content excluding padding and border", "border-box includes padding and border in declared width"),
                "The CSS Box Model consists of four concentric rectangles: Content, Padding, Border, and Margin. With 'box-sizing: content-box' (default), specified width and height apply only to content; padding and borders are added on top, expanding total rendered size. With 'box-sizing: border-box', padding and border are included inside the declared width, making layout math predictable.",
                "Why is applying '* { box-sizing: border-box; }' considered an industry-wide best practice in CSS?",
                List.of("predictable", "layout", "responsive", "overflow", "reset")
        ));

        add(new Question(
                "WEB-BEG-02", JobRole.WEB_DEVELOPER, DifficultyLevel.BEGINNER, "JavaScript Fundamentals",
                "What is the difference between 'var', 'let', and 'const' in modern JavaScript?",
                List.of("var", "let", "const", "hoisting", "scope", "block scope", "reassign"),
                List.of("var is function-scoped and hoisted with undefined", "let and const are block-scoped and have a temporal dead zone", "const cannot be reassigned"),
                "'var' is function-scoped and hoisted to the top of its scope initialized as undefined. 'let' and 'const' were introduced in ES6; they are block-scoped (confined to {}) and exist in a Temporal Dead Zone (TDZ) before declaration, preventing use prior to definition. 'let' can be reassigned, while 'const' identifiers cannot be reassigned (though object properties can still be mutated).",
                "What is the Temporal Dead Zone (TDZ) in JavaScript?",
                List.of("temporal dead zone", "tdz", "referenceerror", "declaration", "initialization")
        ));

        add(new Question(
                "WEB-BEG-03", JobRole.WEB_DEVELOPER, DifficultyLevel.BEGINNER, "DOM & Events",
                "Explain Event Bubbling, Event Capturing, and how Event Delegation works in the browser DOM.",
                List.of("event bubbling", "event capturing", "event delegation", "target", "stoppropagation", "listener"),
                List.of("event flows down in capturing phase and bubbles up from target in bubbling phase", "event delegation places single listener on parent to handle child events via event.target", "greatly reduces memory overhead"),
                "When a DOM event fires, it enters the Capturing phase (trickling down from window to target) and then the Bubbling phase (bubbling up from target back to window). Event Delegation leverages bubbling by attaching a single event listener to a common ancestor element instead of individual child nodes, inspecting 'event.target' to handle events dynamically and save memory.",
                "How does 'event.stopPropagation()' differ from 'event.preventDefault()'?",
                List.of("stoppropagation", "preventdefault", "bubble", "default action", "cancel")
        ));

        add(new Question(
                "WEB-BEG-04", JobRole.WEB_DEVELOPER, DifficultyLevel.BEGINNER, "Asynchronous JavaScript",
                "What is a Promise in JavaScript, and how do 'async/await' make asynchronous code cleaner than callbacks?",
                List.of("promise", "async", "await", "callback hell", "resolve", "reject", "try catch"),
                List.of("promise represents eventual completion of asynchronous operation with pending/fulfilled/rejected states", "async/await provides synchronous-looking syntax with try/catch", "eliminates nested callback hell"),
                "A Promise is an object representing the eventual completion or failure of an asynchronous operation, existing in one of three states: pending, fulfilled, or rejected. While promises can be chained using .then() and .catch(), 'async/await' is syntactic sugar that allows writing asynchronous code with clean, linear, synchronous-looking control flow and standard try/catch error handling, avoiding callback hell.",
                "What happens if an 'await' operation rejects and isn't wrapped in a try/catch block?",
                List.of("unhandledrejection", "unhandled", "crash", "error", "catch")
        ));

        add(new Question(
                "WEB-BEG-05", JobRole.WEB_DEVELOPER, DifficultyLevel.BEGINNER, "Responsive Design",
                "Compare CSS Flexbox and CSS Grid. When would you use Flexbox vs Grid?",
                List.of("flexbox", "grid", "1d", "2d", "one-dimensional", "two-dimensional", "layout"),
                List.of("flexbox is 1-dimensional for rows OR columns", "grid is 2-dimensional for rows AND columns simultaneously", "flexbox for component alignment, grid for page layout"),
                "CSS Flexbox is one-dimensional, designed for aligning and distributing space among items along a single axis (row OR column), making it ideal for navbars, toolbars, and button groups. CSS Grid is two-dimensional, designed for placing items across rows AND columns simultaneously, making it ideal for overall page layouts and complex data grids.",
                "How does the CSS Grid 'fr' fractional unit work?",
                List.of("fr", "fractional", "available space", "proportion", "grid-template-columns")
        ));

        // Intermediate (5)
        add(new Question(
                "WEB-INT-01", JobRole.WEB_DEVELOPER, DifficultyLevel.INTERMEDIATE, "Frontend Performance",
                "What are Core Web Vitals (LCP, INP/FID, CLS), and what techniques optimize them?",
                List.of("core web vitals", "lcp", "cls", "inp", "largest contentful paint", "cumulative layout shift", "performance"),
                List.of("lcp measures loading performance (render hero image/text fast)", "cls measures visual stability (reserve image dimensions)", "inp measures user interaction responsiveness (minimize main thread blocking)"),
                "Core Web Vitals are Google metrics for user experience: 1) LCP (Largest Contentful Paint, target < 2.5s) measures render speed of main content—optimized with CDN, WebP/AVIF images, and preloading; 2) CLS (Cumulative Layout Shift, target < 0.1) measures visual stability—prevented by setting explicit width/height on images and dynamic ads; 3) INP (Interaction to Next Paint, target < 200ms) measures responsiveness—optimized by splitting long JS tasks.",
                "How can the CSS property 'font-display: swap' impact Cumulative Layout Shift?",
                List.of("foit", "fout", "font-display", "fallback", "layout shift")
        ));

        add(new Question(
                "WEB-INT-02", JobRole.WEB_DEVELOPER, DifficultyLevel.INTERMEDIATE, "Web Security",
                "Explain Cross-Site Scripting (XSS) and Cross-Site Request Forgery (CSRF). How do you defend against both in web applications?",
                List.of("xss", "csrf", "samesite", "content security policy", "csp", "sanitization", "csrf token"),
                List.of("xss injects malicious scripts into victim browsers; defend with sanitization, encoding, and csp", "csrf tricks authenticated users into submitting unauthorized actions; defend with samesite cookies and anti-csrf tokens"),
                "XSS occurs when malicious JavaScript is injected into trusted web pages (stored, reflected, or DOM-based); prevention involves contextual HTML escaping, input sanitization, and Content Security Policy (CSP) headers. CSRF tricks authenticated users into executing unwanted state-changing actions; defenses include Anti-CSRF tokens in headers and configuring session cookies with SameSite=Lax/Strict and HttpOnly flags.",
                "Why does setting the 'HttpOnly' flag on cookies protect against XSS token theft?",
                List.of("httponly", "document.cookie", "javascript access", "token theft", "protection")
        ));

        add(new Question(
                "WEB-INT-03", JobRole.WEB_DEVELOPER, DifficultyLevel.INTERMEDIATE, "State Management & Reactivity",
                "Explain the concept of the Virtual DOM and reconciliation in modern frontend frameworks (e.g. React/Vue).",
                List.of("virtual dom", "reconciliation", "diffing algorithm", "rendering", "keys", "performance"),
                List.of("virtual dom is an in-memory representation of real dom", "reconciliation diffs new vdom tree with previous tree to calculate minimal batch updates", "key attributes optimize list diffing"),
                "The Virtual DOM is a lightweight JavaScript object representation of the actual DOM tree. When application state changes, a new VDOM tree is generated. The framework's reconciliation/diffing algorithm compares the new tree with the previous one, calculates the minimal set of required modifications (patches), and batches updates to the real DOM, avoiding expensive layout recalcs.",
                "Why is using array index as a 'key' prop dangerous when rendering dynamic lists in React?",
                List.of("key", "index", "reorder", "state bug", "reconciliation")
        ));

        add(new Question(
                "WEB-INT-04", JobRole.WEB_DEVELOPER, DifficultyLevel.INTERMEDIATE, "Modern JS Engine Internals",
                "Explain how the JavaScript Event Loop coordinates the Call Stack, Microtask Queue (Promises), and Macrotask Queue (setTimeout).",
                List.of("event loop", "call stack", "microtask", "macrotask", "task queue", "settimeout", "promise"),
                List.of("call stack executes synchronous code", "microtask queue (promises, queueMicrotask) has higher priority and drains completely after each call stack tick", "macrotask queue (settimeout, i/o) runs one task per cycle"),
                "JavaScript is single-threaded. Synchronous code executes on the Call Stack. When asynchronous tasks finish, their callbacks enter either the Microtask Queue (Promises, MutationObserver, queueMicrotask) or Macrotask Queue (setTimeout, setInterval, I/O). After the Call Stack empties, the Event Loop completely empties the entire Microtask Queue before picking the next single macrotask.",
                "What is the execution order of: console.log(1); setTimeout(() => console.log(2), 0); Promise.resolve().then(() => console.log(3)); console.log(4);?",
                List.of("1, 4, 3, 2", "order", "execution", "priority", "microtask first")
        ));

        add(new Question(
                "WEB-INT-05", JobRole.WEB_DEVELOPER, DifficultyLevel.INTERMEDIATE, "Networking & Browser Storage",
                "Compare Cookies, LocalStorage, SessionStorage, and IndexedDB in terms of capacity, lifecycle, and security.",
                List.of("cookies", "localstorage", "sessionstorage", "indexeddb", "storage", "capacity", "lifecycle"),
                List.of("cookies: 4kb, sent with http requests, supports httponly", "localstorage: 5-10mb, persists until cleared, synchronous", "sessionstorage: cleared when tab closes", "indexeddb: hundreds of mb, asynchronous nosql database"),
                "Cookies are limited to ~4KB, sent automatically with HTTP requests, and can be secured with HttpOnly/Secure flags. LocalStorage holds ~5-10MB and persists across sessions, but is synchronous and vulnerable to XSS. SessionStorage has similar size but expires when the browser tab closes. IndexedDB provides hundreds of megabytes of asynchronous transactional NoSQL object storage suitable for large offline PWAs.",
                "Which storage mechanism should you use for storing offline application data in a Progressive Web App (PWA)?",
                List.of("indexeddb", "service worker", "offline", "cache api", "asynchronous")
        ));

        // Advanced (5)
        add(new Question(
                "WEB-ADV-01", JobRole.WEB_DEVELOPER, DifficultyLevel.ADVANCED, "Rendering Architectures",
                "Compare CSR, SSR, SSG, and ISR (Incremental Static Regeneration). When would you choose each for an enterprise platform?",
                List.of("csr", "ssr", "ssg", "isr", "next.js", "seo", "time to first byte", "hydration"),
                List.of("csr renders in browser, fast transitions but poor initial seo", "ssr renders on each request, great seo and fresh data", "ssg builds static html at build time, ultra fast cdn delivery", "isr updates static pages in background without full rebuilds"),
                "Client-Side Rendering (CSR) downloads empty HTML and builds UI in browser, ideal for private interactive dashboards. Server-Side Rendering (SSR) generates HTML per request, ideal for user-tailored dynamic pages requiring real-time SEO. Static Site Generation (SSG) compiles HTML at build time for maximum CDN speed (documentation, blogs). Incremental Static Regeneration (ISR) serves cached static pages and regenerates them in the background upon invalidation, giving the best blend of scale, speed, and freshness for e-commerce.",
                "What is 'hydration' in SSR frameworks, and why is hydration overhead a performance challenge?",
                List.of("hydration", "event listeners", "vdom", "cpu", "main thread")
        ));

        add(new Question(
                "WEB-ADV-02", JobRole.WEB_DEVELOPER, DifficultyLevel.ADVANCED, "Browser Internals & Rendering Pipeline",
                "Explain the Browser Critical Rendering Path from HTML tokens to Painting, and how CSS/JS trigger Reflow (Layout) vs Repaint.",
                List.of("critical rendering path", "dom", "cssom", "render tree", "layout", "reflow", "repaint", "composite"),
                List.of("html/css tokens are parsed into dom and cssom trees", "combined into render tree for visible elements", "layout calculates geometry; paint fills pixels; composite layers textures on gpu", "transform and opacity skip layout and repaint for 60fps animations"),
                "The browser parses HTML tokens into the DOM and CSS into the CSSOM, combining them into the Render Tree. The Layout (Reflow) stage calculates exact geometric coordinates for each element. The Paint stage renders pixel colors, text, and shadows into bitmap layers. Finally, the Compositor sends layers to the GPU. Modifying geometric properties (width, margin) causes expensive Layout recalculations; modifying 'transform' or 'opacity' skips layout and repaint directly to composite on the GPU for butter-smooth 60fps.",
                "How does CSS 'will-change' hint help browser GPU compositing?",
                List.of("will-change", "layer", "gpu", "compositing", "memory")
        ));

        add(new Question(
                "WEB-ADV-03", JobRole.WEB_DEVELOPER, DifficultyLevel.ADVANCED, "Progressive Web Apps & Service Workers",
                "How do Service Workers operate outside the main thread, and how do you implement an offline-first caching strategy like Stale-While-Revalidate?",
                List.of("service worker", "offline", "cache api", "stale-while-revalidate", "background sync", "lifecycle"),
                List.of("service workers run in background worker thread intercepting network requests", "stale-while-revalidate serves cached response instantly while fetching fresh version in background", "enables instant load and offline resilience"),
                "A Service Worker is an event-driven programmable proxy running in a worker thread separate from the DOM. It intercepts fetch requests. In the Stale-While-Revalidate pattern, the service worker immediately returns cached response data to the user for instant perceived performance, simultaneously issuing a background fetch to update the Cache API with fresh data for future requests.",
                "What are the three main lifecycle phases of a Service Worker?",
                List.of("registration", "installation", "activation", "activate", "install")
        ));

        add(new Question(
                "WEB-ADV-04", JobRole.WEB_DEVELOPER, DifficultyLevel.ADVANCED, "Micro-Frontends & Module Federation",
                "How does Webpack Module Federation enable independent teams to build and deploy Micro-Frontends without iframe latency or monolithic bundles?",
                List.of("micro-frontends", "module federation", "webpack", "host", "remote", "shared dependencies", "runtime"),
                List.of("allows applications to dynamically import remote code modules at runtime", "shares singleton dependencies like react to prevent duplicate libraries", "enables independent builds and deployments across teams"),
                "Webpack Module Federation allows an application (Host) to dynamically import modules and components from another independently deployed build (Remote) at runtime over the network without bundling them together at compile time. It features shared dependency negotiation, allowing Host and Remote to share singletons (like React or UI design system libraries) to prevent duplicate download overhead while enabling independent CI/CD deployments.",
                "How does Module Federation handle version conflicts when the Host and Remote request different semver versions of a shared dependency?",
                List.of("singleton", "semver", "fallback", "resolution", "conflict")
        ));

        add(new Question(
                "WEB-ADV-05", JobRole.WEB_DEVELOPER, DifficultyLevel.ADVANCED, "Modern Web Standards & WebAssembly",
                "What is WebAssembly (Wasm), how does its stack-based bytecode execute alongside JavaScript, and when should you adopt it?",
                List.of("webassembly", "wasm", "bytecode", "performance", "linear memory", "c++", "rust", "simd"),
                List.of("wasm compiles high-performance languages (rust, c++) into binary bytecode", "runs near-native speed in browser sandbox", "ideal for intensive tasks like video editing, 3d rendering, and cryptography"),
                "WebAssembly (Wasm) is a low-level binary code format that executes at near-native speed inside the browser sandbox with direct access to linear memory. It operates alongside JavaScript through a shared memory buffer and JavaScript API bindings. It is not designed to replace HTML/CSS, but rather to offload computationally heavy operations—such as video/audio editing, CAD rendering, Figma-style canvas manipulation, game engines, and cryptography.",
                "How does memory sharing between JavaScript and a WebAssembly module work?",
                List.of("webassembly.memory", "arraybuffer", "linear memory", "typedarray", "pointer")
        ));
    }
}
