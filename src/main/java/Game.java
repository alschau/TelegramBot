import java.io.Serializable;
import java.util.*;

//##########################################################################################
//
// Created by Aline Schaufelberger on 01.06.2019 - 20:24
//
//##########################################################################################


public class Game implements Serializable {
    private static final long serialVersionUID = 1L;

    Person fragesteller;
    ArrayList<Person> player;
    String frage = "";
    Integer votes = 0;
    ArrayList<Antwort> antworten = new ArrayList<>();
    Person[] rankings = new Person[20];

    JamesBot bot;
    Antwort masterantwort;

    public Boolean readyToVote = false;

    ArrayList<String> correct = new ArrayList<String>();
    ArrayList<String> wrong = new ArrayList<String>();


    public Game(ArrayList<Person> player, Person startPerson, JamesBot bot) {
        correctFill();
        wrongFill();
        this.fragesteller = startPerson;
        this.player = player;
        this.bot = bot;
        for (int i=0; i<player.size(); i++){
            rankings[i]=player.get(i);
        }
        getFrage();
        if (masterantwort != null && this.antworten.size() == player.size()-1){
            shuffleAndSend();
        }
    }

    public void getFrage() {
        sendNachrichtAnPlayer("Das Spiel beginnt, " + fragesteller.getUser().getFirstName() + " überlegt sich schon die Frage!");
        bot.sendNachrichtNorm("Deine Frage?", fragesteller.getId());
        fragesteller.fragesteller = true;
    }

    public void sendNachrichtAnPlayer(String nachricht){
        for (Person per : this.player) {
            if (per.getId() != fragesteller.getId()) {
                bot.sendNachrichtNorm(nachricht, per.getId());
            }
        }
    }

    public void sendNachrichtAnAlle(String nachricht){
        for (Person per : this.player) {
            bot.sendNachrichtNorm(nachricht,per.getId());
        }
    }

    public void addAntwort(String antwort, Person per){
        per.antworter = false;
        per.voter = true;
        Antwort ant = new Antwort(per, antwort, false);
        antworten.add(ant);
        if(antworten.size() == player.size()){
            if (this.masterantwort != null){
                sendNachrichtAnAlle("Alle haben ihre Antworten abgegeben!");
                shuffleAndSend();
            }
        }
    }

    public void setFrage(String Frage){
        this.frage = Frage;
        sendNachrichtAnAlle("Die Frage lautet:\n" + frage);
        sendNachrichtAnPlayer("Was ist deine Antwort?");
        for (Person per : this.player) {
            if (per.getId() != fragesteller.getId()) {
                per.antworter = true;
            }
        }
        bot.sendNachrichtNorm("Wie lautet die richtige Antwort?", fragesteller.getId());
    }

    public void setMasterAntwort(String antwort, Person person){
        fragesteller.fragesteller = false;
        System.out.println(fragesteller.getUser().getFirstName());
        System.out.println(antwort);
        Antwort ant = new Antwort(person, antwort,true);
        this.masterantwort = ant;
        antworten.add(ant);
        System.out.println("Set!");
        if (this.antworten.size() == player.size()){
            shuffleAndSend();
        }
    }

    public void shuffleAndSend(){
        Collections.shuffle(antworten);
        for (int i=0; i<this.antworten.size(); i++) {
            antworten.get(i).setI(i);
            sendNachrichtAnAlle(i + " - " + antworten.get(i).getAntwort());
        }
        sendNachrichtAnPlayer("Zeit für die Abstimmung! \n Gib die Nummer der Antwort ein, für die Du stimmst.");
        readyToVote = true;
    }

    public void Vote(int i, Person person){
        person.voter = false;
        votes++;
        for (Antwort ant : this.antworten){
            if (i == ant.getI()){
                if (ant.isMaster()){
                    person.addPointsRichtig();
                    ant.addVoter(person);
                    System.out.println(person.getUser().getFirstName() + " chose wisely!");
                } else {
                    if (person != ant.getPerson()) {
                        ant.getPerson().addPointsVerarsche();
                    }
                    System.out.println(person.getUser().getFirstName() + " has been fooled!");
                    ant.addVoter(person);
                }
            }
        }
        ranking(rankings);
        if (votes == this.antworten.size() -1){
            checkAnswers();
        }
    }

    public void checkAnswers() {
        for (Antwort ant : this.antworten){
            // Voters von Antworten
            if(!ant.isMaster() && ant.getMyVoters().size()!=0) {
                if (ant.getMyVoters().size()==1 && ant.getMyVoters().get(0) == ant.getPerson()){
                    bot.sendNachrichtNorm("Dumm, dümmer, "+ant.getPerson().getUser().getFirstName()+".", ant.getPerson().getId());
                }
                if (ant.getMyVoters().size() >1) {
                    bot.sendNachrichtNorm("Du hast diese Personen verarscht: ", ant.getPerson().getId());
                    for (Person person : ant.getMyVoters()) {
                        if (person != ant.getPerson()) {
                            bot.sendNachrichtNorm(person.user.getFirstName(), ant.getPerson().getId());
                            // choose random string from wrong and send to that person
                            String str = getWrong();
                            String strName = str.replace("£", person.getUser().getFirstName());
                            bot.sendNachrichtNorm(strName, person.getId());
                        } else {
                            bot.sendNachrichtNorm("Dich selber du Genie", ant.getPerson().getId());
                        }
                    }
                }

            // Voters von MasterAntwort
            } else if (ant.isMaster()){
                switch (ant.getMyVoters().size()){
                    case 0:
                        bot.sendNachrichtNorm("Niemand wusste die Antwort!", ant.getPerson().getId());
                        break;
                    case 1:
                        bot.sendNachrichtNorm("Nur "+ant.getMyVoters().get(0).getUser().getFirstName()+
                                " Wusste die Antwort.", ant.getPerson().getId());
                        String str1 = getCorrect();
                        String strName1 = str1.replace("£", ant.getMyVoters().get(0).getUser().getFirstName());
                        bot.sendNachrichtNorm(strName1, ant.getMyVoters().get(0).getUser().getId());
                        break;
                    default:
                        bot.sendNachrichtNorm("Diese Personen haben richtig geantwortet: ", ant.getPerson().getId());
                        for (Person person : ant.getMyVoters()) {
                            bot.sendNachrichtNorm(person.user.getFirstName(), ant.getPerson().getId());
                            // choose random string from correct and send to person
                            String str = getCorrect();
                            String strName = str.replace("£", person.getUser().getFirstName());
                            bot.sendNachrichtNorm(strName, person.getId());
                        }
                }
            }
        }
    }

    public void ranking(Person[] l){
        sort(l);
    }

    public void sort(Person[] rankingList) {
        int n = rankingList.length;
        for (int i = 0; i < n - 1; i++) {
            if (rankingList[i]!= null) {
                for (int j = 0; j < n - i - 1; j++) {
                    if (rankingList[j]!= null && rankingList[j+1] != null){
                        if (rankingList[j].getPoints() < rankingList[j + 1].getPoints()) {
                            Person temp = rankingList[j];
                            rankingList[j] = rankingList[j + 1];
                            rankingList[j + 1] = temp;
                        }
                    } else {
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }

    public void correctFill(){
        correct.add("Richtig");
        correct.add("Hurra, das war richtig! \uD83D\uDE0E");
        correct.add("Top! Weiter so.");
        correct.add("Richtig, dir macht keiner was vor.");
        correct.add("Ausgezeichnete Arbeit.");
        correct.add("£, du bist spitze!");
        correct.add("Mach weiter so.");
        correct.add("Absolut korrekt.");
        correct.add("100 Punkte für Gryffindor!");
        correct.add("Richtig, £ du bist der King.");
        correct.add("Du kleiner Sherlock Holmes \uD83D\uDD0D");
        correct.add("Du bist schlau wie ein Rotfuchs.");
        correct.add("Right");
        correct.add("c'était juste, mon ami.");
        correct.add("Super weiter so.");
        correct.add("Das gibt dir weitere 2 Punkte.");
        correct.add("Du bist Gold wert.");
        correct.add("Easy Peasy Lemon Squeezy");
        correct.add("Oida stimmt!");
        correct.add("Schummelt da wer oder bist du einfach so gut?");

    }
    public void wrongFill(){
        wrong.add("Falsch");
        wrong.add("Leider falsch, das geht noch besser.");
        wrong.add("Das war die falsche Antwort.");
        wrong.add("Knapp daneben ist auch vorbei.");
        wrong.add("Aber £, das kannst doch unmöglich stimmen.");
        wrong.add("Da hat dir wohl jemand einen Bären aufgebunden.");
        wrong.add("£, du wurdest schon wieder in die irre geleitet.");
        wrong.add("Tut mir leid, du wurdest überlistet.");
        wrong.add("Hattest du in der Schule einen Fensterplatz?");
        wrong.add("NAME, benutz dein Kopf!");
        wrong.add("\uD83D\uDCA9");
        wrong.add("Tja, vielleicht hast du nächstes mal mehr glück.");
        wrong.add("£, das war falsch.");
        wrong.add("Hätte sein können, stimmt aber nicht.");
        wrong.add("Sorry £, aber das war peinlich.");
    }

    public String getCorrect(){
        Collections.shuffle(correct);
        return correct.get(0);
    }

    public String getWrong(){
        Collections.shuffle(wrong);
        return wrong.get(0);
    }

    public Person getFragesteller() {
        return fragesteller;
    }

    public void setFragesteller(Person fragesteller) {
        this.fragesteller = fragesteller;
    }


    public ArrayList<Person> getPlayer() {
        return player;
    }
}
