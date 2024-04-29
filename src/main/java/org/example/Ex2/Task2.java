package org.example.Ex2;

import java.util.concurrent.Phaser;

/* Вы пишете книгу и вы автор. Есть 2 человека, которые выступают у вас соавторами.
 На этапе написания книги, они пишут разные главы, помогая таким образом вам полностью ее закончить.
 После того как вы и все соавторы закончили свою работу, вы отправляете книгу на рецензирование.
 В группу рецензентов входит 3 человека, каждый из которых является специалистом в своей области
 и проверяет Вашу книгу на правильность отражения фактов в вашем произведении.
 После того как все рецензии получены, Вы отправляете книгу в издательский дом.
 В редакции главный редактор и руководитель издательства читают ваше творение и утверждают
 его в печать.

 Вы относите утвержденные рукописи печатникам, они печатают книгу, переплетчики делают ей переплет,
 а служба доставки развозят книги по магазинам.

 Поздравляю Вас, вы полностью прошли все фазы становления известного писателя, т.к. Ваша книга
 стала бестселлером! )
 Создайте приложение, которое сымитирует работу процесса создания книги, с учетом, что каждый человек,
 который встречается в описанной схеме будет представлен отдельным потоком.
 Какой синхронизатор с библиотеки concurrent мог бы быть Вам полезен при выполнении данной задачи? */
public class Task2 {
    public static void main(String[] args) {
        Phaser phaser = new Phaser(1);
        int curPhase;

        System.out.println("Начинаем писать книгу");

        new Thread(new Author(phaser, "Валера")).start();
        new Thread(new Co_author1(phaser, "Анатолий")).start();
        new Thread(new Co_author2(phaser, "Степан")).start();

        curPhase = phaser.getPhase();
        phaser.arriveAndAwaitAdvance();
        System.out.println("Этап " + (curPhase + 1) + " окончен, отправляем книгу на рецензирование");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(new Reviewer1("Тамара", phaser)).start();
        new Thread(new Reviewer2("Зоя", phaser)).start();
        new Thread(new Reviewer3("Гриша", phaser)).start();

        curPhase = phaser.getPhase();
        phaser.arriveAndAwaitAdvance();
        System.out.println("Этап " + (curPhase + 1) + " окончен, отправляем книгу в издательский дом");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(new ChiefEditor("Хуан", phaser)).start();
        new Thread(new HeadPublishingHouse("Хосе", phaser)).start();

        curPhase = phaser.getPhase();
        phaser.arriveAndAwaitAdvance();
        System.out.println("Этап " + (curPhase + 1) + " окончен, отправляем книгу печатникам");

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(new Printer1("Стив", phaser)).start();
        new Thread(new Printer2("Роберт", phaser)).start();

        curPhase = phaser.getPhase();
        phaser.arriveAndAwaitAdvance();
        System.out.println("Этап " + (curPhase + 1) + " окончен, отправляем книгу на переплёт");

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(new Bookbinder1("Толя", phaser)).start();
        new Thread(new Bookbinder2("Оля", phaser)).start();

        curPhase = phaser.getPhase();
        phaser.arriveAndAwaitAdvance();
        System.out.println("Этап " + (curPhase + 1) + " окончен, отправляем книгу на доставку");

        System.out.println("Мы закончили возится с книгой");
    }
}

class Author implements Runnable {
    Phaser phaser;
    private String nameAuthor;

    public Author(Phaser phaser, String nameAuthor) {
        this.phaser = phaser;
        this.nameAuthor = nameAuthor;
        phaser.register();
    }

    @Override
    public void run() {
        System.out.println("Главный редактор " + nameAuthor + " написал 12 глав");
        phaser.arriveAndAwaitAdvance();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        phaser.arriveAndDeregister();
    }
}

class Co_author2 implements Runnable {
    Phaser phaser;
    private String nameAuthor;

    public Co_author2(Phaser phaser, String nameAuthor) {
        this.phaser = phaser;
        this.nameAuthor = nameAuthor;
        phaser.register();
    }

    @Override
    public void run() {
        System.out.println("Соавтор " + nameAuthor + " написал 3 главы");
        phaser.arriveAndAwaitAdvance();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        phaser.arriveAndDeregister();
    }
}

class Co_author1 implements Runnable {
    Phaser phaser;
    private String nameAuthor;

    public Co_author1(Phaser phaser, String nameAuthor) {
        this.phaser = phaser;
        this.nameAuthor = nameAuthor;
        phaser.register();
    }

    @Override
    public void run() {
        System.out.println("Соавтор " + nameAuthor + " написал 4 главы");
        phaser.arriveAndAwaitAdvance();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        phaser.arriveAndDeregister();
    }
}

class Reviewer1 implements Runnable {
    private String nameReviewer;
    Phaser phaser;

    public Reviewer1(String nameReviewer, Phaser phaser) {
        this.nameReviewer = nameReviewer;
        this.phaser = phaser;
        phaser.register();
    }

    @Override
    public void run() {
        System.out.println("Рецензент " + nameReviewer + " проверил книгу, всё хорошо!");
        phaser.arriveAndAwaitAdvance();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        phaser.arriveAndDeregister();
    }
}

class Reviewer2 implements Runnable {
    private String nameReviewer;
    Phaser phaser;

    public Reviewer2(String nameReviewer, Phaser phaser) {
        this.nameReviewer = nameReviewer;
        this.phaser = phaser;
        phaser.register();
    }

    @Override
    public void run() {
        System.out.println("Рецензент " + nameReviewer + " проверил книгу, всё хорошо!");
        phaser.arriveAndAwaitAdvance();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        phaser.arriveAndDeregister();
    }
}

class Reviewer3 implements Runnable {
    private String nameReviewer;
    Phaser phaser;

    public Reviewer3(String nameReviewer, Phaser phaser) {
        this.nameReviewer = nameReviewer;
        this.phaser = phaser;
        phaser.register();
    }

    @Override
    public void run() {
        System.out.println("Рецензент " + nameReviewer + " проверил книгу, всё хорошо!");
        phaser.arriveAndAwaitAdvance();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        phaser.arriveAndDeregister();
    }
}

class ChiefEditor implements Runnable {
    private String nameChiefEditor;
    Phaser phaser;

    public ChiefEditor(String nameChiefEditor, Phaser phaser) {
        this.nameChiefEditor = nameChiefEditor;
        this.phaser = phaser;
        phaser.register();
    }

    @Override
    public void run() {
        System.out.println("Главный редактор " + nameChiefEditor + " прочитал ваше творение и утвердил книгу в печать!");
        phaser.arriveAndAwaitAdvance();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        phaser.arriveAndDeregister();
    }
}

class HeadPublishingHouse implements Runnable {
    private String nameHeadPublishingHouse;
    Phaser phaser;

    public HeadPublishingHouse(String nameHeadPublishingHouse, Phaser phaser) {
        this.nameHeadPublishingHouse = nameHeadPublishingHouse;
        this.phaser = phaser;
        phaser.register();
    }

    @Override
    public void run() {
        System.out.println("Руководитель издательства " + nameHeadPublishingHouse + " прочитал ваше творение и утвердил книгу в " +
                "печать!");
        phaser.arriveAndAwaitAdvance();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        phaser.arriveAndDeregister();
    }
}

class Printer1 implements Runnable {
    private String namePrinter1;
    Phaser phaser;

    public Printer1(String namePrinter1, Phaser phaser) {
        this.namePrinter1 = namePrinter1;
        this.phaser = phaser;
        phaser.register();
    }

    @Override
    public void run() {
        System.out.println("Печатник " + namePrinter1 + " печатает книгу ");
        phaser.arriveAndAwaitAdvance();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        phaser.arriveAndDeregister();
    }
}

class Printer2 implements Runnable {
    private String namePrinter2;
    Phaser phaser;

    public Printer2(String namePrinter2, Phaser phaser) {
        this.namePrinter2 = namePrinter2;
        this.phaser = phaser;
        phaser.register();
    }

    @Override
    public void run() {
        System.out.println("Печатник " + namePrinter2 + " печатает книгу ");
        phaser.arriveAndAwaitAdvance();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        phaser.arriveAndDeregister();
    }
}

class Bookbinder1 implements Runnable {
    private String nameBookbinder1;
    Phaser phaser;

    public Bookbinder1(String namePrinter2, Phaser phaser) {
        this.nameBookbinder1 = namePrinter2;
        this.phaser = phaser;
        phaser.register();
    }

    @Override
    public void run() {
        System.out.println("Переплётчик " + nameBookbinder1 + " переплетает книгу");
        phaser.arriveAndAwaitAdvance();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        phaser.arriveAndDeregister();
    }
}

class Bookbinder2 implements Runnable {
    private String nameBookbinder2;
    Phaser phaser;

    public Bookbinder2(String nameBookbinder2, Phaser phaser) {
        this.nameBookbinder2 = nameBookbinder2;
        this.phaser = phaser;
        phaser.register();
    }

    @Override
    public void run() {
        System.out.println("Переплётчик " + nameBookbinder2 + " переплетает книгу");
        phaser.arriveAndAwaitAdvance();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        phaser.arriveAndDeregister();
    }
}