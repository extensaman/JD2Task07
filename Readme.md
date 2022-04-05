<h2>Task condition</h2>

There is a Person entity, it has 
- an identifier; 
- a first name 
- a last name.

<p>The task is to create a project. Adding a table to the database must be done through liquibase,
make tests using H2.</p> 
<p>Cover functionality with tests and make a report using the jacoco plugin.</p>
<p>Connect checkstyle to the project.</p>
<p>For the Person entity, make a DAO over each field, write your own annotation MyColumn(name - the name of the column) with the name of the column, write the annotation MyTable(name - the name of the table) above the Person class. Implement CRUD operations on Person using jdbc</p>

- select
- update
- delete
- insert

<p>Moreover, these operations should make a request to the database using the annotations MyColumn and MyTable (through reflection). i.e. if the user of this API creates another entity, then</p>

- select
- update
- delete
- insert 
<p>should work without changing the internal logic</p>

<hr>

<h3>Условия задачи</h3>
Есть сущность Person, у нее есть 
- индификатор, 
- имя 
- фамилия.
<p>Задание создать проект.</p>
<p>Добавление таблицы в базу необходимо сделать через liquibase,
сделать тесты используя H2. Покрыть функционал тестами
и сделать отчет используя плагин jacoco.
Подключить checkstyle к проекту.</p>
<p>Для сущности Person сделать DAO над каждым полем написать свою
аннотацию  MyColumn(name - название колонки) с названием колонки, над классом Person написать аннотацию MyTable(name - название таблицы). Реализовать CRUD операции с Person используя jdbc</p>

- select
- update
- delete
- insert
<p>Причем эти операции должны составлять запрос в базу используя аннотации MyColumn и MyTable (через рефлексию). T.е. если пользователь данного API создаст другую сущность то</p>

- select
- update
- delete
- insert 
<p>должны работать без изменения внутреней логики</p>