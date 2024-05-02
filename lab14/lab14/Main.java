package lab14;

import lab14lib.Generator;
import lab14lib.GeneratorAudioVisualizer;

public class Main {
    public static void main(String[] args) {

        /*
        Generator g1 = new SineWaveGenerator(200);
        Generator g2 = new SineWaveGenerator(201);
        ArrayList<Generator> generators = new ArrayList<Generator>();
        generators.add(g1);
        generators.add(g2);
        MultiGenerator mg = new MultiGenerator(generators);
        GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(mg);
        gav.drawAndPlay(500000, 1000000);
         */

        /* test SawTooth */
//        Generator generator = new SawToothGenerator(512);
//        GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
//        gav.drawAndPlay(4096, 1000000);

        /* test AcceleratingSawToothGenerator*/
//        Generator generator = new AcceleratingSawToothGenerator(200, 1.10);
//        GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
//        gav.drawAndPlay(4096, 1000000);

        /* test StrangeBitwiseGenerator */
        Generator generator = new StrangeBitwiseGenerator(1024);
        GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
        gav.drawAndPlay(128000, 1000000);


    }
}
