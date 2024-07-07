package javaKarol;

import javakarol.Roboter;
import javakarol.Welt;

public class WeltMitRoboter {
    private Welt welt;
    private Roboter roboter;
    private int gelegteZiegel;
    private int anzahlZuLegendeZiegel;
    private boolean rundeAbgeschlossen;
    private int startPositionX;
    private int startPositionY;
    private boolean zeitungGeholt;
    private boolean exitRight;
    private boolean exitLeft;


    public WeltMitRoboter(int breite, int laenge, int hoehe) {
        welt = new Welt(breite, laenge, hoehe);
        roboter = new Roboter(welt);
//		roboter.VerzoegerungSetzen(10);
    }

    public WeltMitRoboter(String absoluterDateipfad) {
        welt = new Welt(absoluterDateipfad);
        roboter = new Roboter(welt);
    }

    public void roboterSchrittVorwärts() {
        roboter.Schritt();
    }

    public void roboterMarkeSetzen() {
        roboter.MarkeSetzen();
    }

    public void roboterZiegelAufheben() {
        roboter.Aufheben();
    }

    public void roboterZiegelHinlegen() {
        roboter.Hinlegen();
    }

    public void roboterLinksDrehen() {
        roboter.LinksDrehen();
    }

    public void roboterRechtsDrehen() {
        roboter.RechtsDrehen();
    }

    public void weltReset() {
        welt.ZurueckSetzen();
    }

    public void weltSpeichern(String dateiname) {
        welt.Speichern(dateiname);
    }

    public void schrittVorWandGehen() {
        if (roboter.IstWand()) {
            System.out.println("Roboter steht an der Wand");
        }
    }

    public void roboterBisWandgehen() {
        while (!roboter.IstWand()) {
            roboter.Schritt();
        }
    }

    public void roboterInStartPosition() {

        while (!roboter.IstBlickNorden()) {
            roboter.RechtsDrehen();
        }
        roboterBisWandgehen();
    }

    public void turnAround () {
        roboter.RechtsDrehen();
        roboter.RechtsDrehen();
    }


    public void roboterReiheZiegelLegen() {
       gelegteZiegel=0;

        switch (roboter.BlickrichtungGeben()) {
            case 'N':
            case 'S':
                anzahlZuLegendeZiegel = welt.getWeltLaenge();

            case 'O':
            case 'W':
                anzahlZuLegendeZiegel = welt.getWeltBreite();
        }

        // Wenn Reihe noch nicht vollständig gelegt
        do {

            if (roboter.IstWand()) {
                turnAround();

            } else {

                if (!roboter.IstZiegel()) {
                    roboter.Hinlegen();
                    gelegteZiegel++;
                }
                roboter.Schritt();

                if (roboter.IstWand()) {
                    turnAround();
                }
            }

        } while  (!(anzahlZuLegendeZiegel == gelegteZiegel));
    }

    public void roboterDurchSumpfLaufen () {
        rundeAbgeschlossen=false;

        startPositionX = roboter.PositionXGeben();
        startPositionY = roboter.PositionYGeben();

        while (!rundeAbgeschlossen) {

            while (roboter.IstZiegel()) {
                roboter.Schritt();
            }

            if (!roboter.IstZiegel()) {

                if (roboter.IstZiegelLinks()) {
                    roboter.LinksDrehen();
                } else {
                    roboter.RechtsDrehen();
                }
            }

            if (roboter.PositionXGeben() == startPositionX && roboter.PositionYGeben() == startPositionY) {
                rundeAbgeschlossen=true;
            }

        }


    }

    public void findExit () {

        while (!roboter.IstZiegel()) {
            roboter.Schritt();

            if (roboter.IstZiegel() && (roboter.IstZiegelRechts() || roboter.IstZiegelLinks())) {
                turnAround();
            } else if (roboter.IstZiegel()) {
                roboter.RechtsDrehen();
            }

            if (!roboter.IstZiegel() && (!roboter.IstZiegelRechts() && !roboter.IstZiegelLinks())) {
                while (!roboter.IstBlickSueden()) {
                    roboter.RechtsDrehen();
                }
                break;
            }
        }
    }

    public void zuZeitungGehen () {

        while (!roboter.IstMarke()) {
            roboter.Schritt();
        }

        zeitungGeholt=true;
    }

    public void roboterZeitungHolen() {

        // Da Ausgang immer im Süden -> Roboter soll sich erst immer nach Süden drehen
        while (!roboter.IstBlickSueden()) {
            roboter.LinksDrehen();
        }

        // Abfrage, ob Zeitung bereits geholt
        if (!zeitungGeholt) {

            // Rotober sucht selbstständig Ausgang des Gebäudes
            findExit();
            zuZeitungGehen();
        }
    }

    public void roboterRandPflaster () {
        int perimeterRectangleBricks = (welt.getWeltBreite()*2 - 4) + (welt.getWeltLaenge()*2);
        int bricksOnFloor = 0;
        roboterInStartPosition();

        
        while (!roboter.IstBlickSueden()) {
            roboter.RechtsDrehen();
        }

        do {

            if (roboter.IstWand()) {
                roboter.LinksDrehen();
            }

            roboter.Hinlegen();
            roboter.Schritt();
            bricksOnFloor++;

        } while (bricksOnFloor < perimeterRectangleBricks);


    }

}
