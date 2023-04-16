import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class Main {

    public static ArrayBlockingQueue queueA = new ArrayBlockingQueue<>(100);
    public static ArrayBlockingQueue queueB = new ArrayBlockingQueue<>(100);
    public static ArrayBlockingQueue queueC = new ArrayBlockingQueue<>(100);
    public static final int AMOUNT_CHAR = 10_000;
    public static final int LENGTH_TEXT = 100_000;
    public static final String TEXT = "abc";

    public static void main(String[] args) {

        new Thread(() -> {
            for (int i = 0; i < AMOUNT_CHAR; i++){
                String textForQueue = generateText(TEXT, LENGTH_TEXT);
                try {
                    queueA.put(textForQueue);
                    queueB.put(textForQueue);
                    queueC.put(textForQueue);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();

        Thread thread1 = new Thread(() -> {
            char charThread = 'a';
            int maxA = findMaxCount(queueA, charThread);
            System.out.println("Максимальное количество символов «" + charThread +
                    "» в сгенерированном тексте: " + maxA);
        });
        thread1.start();


        Thread thread2 = new Thread(() -> {
            char charThread = 'b';
            int maxB = findMaxCount(queueB, charThread);
            System.out.println("Максимальное количество символов «" + charThread +
                    "» в сгенерированном тексте: " + maxB);
        });
        thread2.start();


        Thread thread3 = new Thread(() -> {
            char charThread = 'c';
            int maxC = findMaxCount(queueC, charThread);
            System.out.println("Максимальное количество символов «" + charThread +
                    "» в сгенерированном тексте: " + maxC);
        });
        thread3.start();

    }

    public static int findMaxCount(ArrayBlockingQueue queue, char letter){
        int max = 0;
        int count = 0;
        String text;

        for (int i = 0; i < AMOUNT_CHAR; i++){
            try {
                text = (String) queue.take();
                for (char c : text.toCharArray()){
                    if ( c == letter){
                        count++;
                    }
                }
                if (count > max){
                    max = count;
                }
                count = 0;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return max;
    }
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}