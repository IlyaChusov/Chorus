package com.johnny.chorus.model;

import com.johnny.chorus.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Lab {

    private static Lab lab;
    private final List<SongsList> songsLists;
    private int id = 0;

    private Lab() {
        songsLists = new ArrayList<>();

        Song[] stikhiry = new Song[5];
        Song stikhira1 = createSong(
                1,
                "Христос, Спаситель наш, / стёр запись грехов наших, пригвоздив её ко Кресту," +
                        " / и владычество Смерти упразднил, // посему мы поклоняемся Его тридневному Воскресению!",
                R.raw.stikhira_1_notes,
                R.raw.stikhira_1_general,

                R.raw.stikhira_1_bass,
                R.raw.stikhira_1_bariton,
                R.raw.stikhira_1_tenor,
                R.raw.stikhira_1_soprano
        );
        stikhiry[0] = stikhira1;

        Song stikhira2 = createSong(
                2,
                "Прежде времён от Отца родившемуся – Богу-Слову, / от Девы Марии воплотившемуся, " +
                        "/ приди́те, покло́нимся, / ведь Он, крест претерпев, / пре́дан был погребению по воле Своей, /" +
                        " и, восстав из мёртвых, // спас меня, заблудшего человека.",
                R.raw.stikhira_1_notes,
                R.raw.stikhira_1_general,

                R.raw.stikhira_1_bass,
                R.raw.stikhira_1_bariton,
                R.raw.stikhira_1_tenor,
                R.raw.stikhira_1_soprano
        );
        stikhiry[1] = stikhira2;

        Song stikhira3 = createSong(
                3,
                "Со архангелами воспоём Воскресение Христа, / ведь Он Избави́тель и Спаситель наших душ; / " +
                        "Он во славе грозной и в могучей силе // снова грядёт судить мир, который созда́л.",
                R.raw.stikhira_1_notes,
                R.raw.stikhira_1_general,

                R.raw.stikhira_1_bass,
                R.raw.stikhira_1_bariton,
                R.raw.stikhira_1_tenor,
                R.raw.stikhira_1_soprano
        );
        stikhiry[2] = stikhira3;

        Song stikhira4 = createSong(
                4,
                "О Тебе, Владыка, распятом и погребённом, / ангел возвестил, сказав мироносицам: / " +
                        "«Подойди́те, посмотрите, где Господь был положен. / Вот, Он, как и говорил, воскрес, ибо всесилен». " +
                        "/ Посему мы поклоняемся Тебе, одному бессмертному. // О Христос, Податель Жизни, помилуй нас!",
                R.raw.stikhira_1_notes,
                R.raw.stikhira_1_general,

                R.raw.stikhira_1_bass,
                R.raw.stikhira_1_bariton,
                R.raw.stikhira_1_tenor,
                R.raw.stikhira_1_soprano
        );
        stikhiry[3] = stikhira4;

        Song stikhira5 = createSong(
                5,
                "Своим Крестом Ты упразднил проклятие, / настигшее нас от древа, / " +
                        "погребением Своим Ты умертвил владычество Смерти, / и Воскресением Своим Ты просветил человеческий род, " +
                        "/ посему мы взываем к Тебе: // «О Благодетель, Христос Бог наш, слава Тебе!»",
                R.raw.stikhira_1_notes,
                R.raw.stikhira_1_general,

                R.raw.stikhira_1_bass,
                R.raw.stikhira_1_bariton,
                R.raw.stikhira_1_tenor,
                R.raw.stikhira_1_soprano
        );
        stikhiry[4] = stikhira5;


        Song[] tropary = new Song[8];
        Song tropar1 = createSong(
                1,
                "Очистим чувства и увидим Христа, / блистающего неприступным светом Воскресения, /" +
                        " и явственно от Него услышим: «Радуйтесь!», // победную песнь воспевая.",
                R.raw.stikhira_1_notes,
                R.raw.tropar_1_general,

                R.raw.stikhira_1_bass,
                R.raw.stikhira_1_bariton,
                R.raw.stikhira_1_tenor,
                R.raw.stikhira_1_soprano
        );
        tropary[0] = tropar1;

        Song tropar2 = createSong(
                2,
                "Так пусть Небеса достойно радуются, / земля же да ликует, / " +
                        "пусть празднует весь мир, видимый и невидимый, / ведь восстал Христос — // веселие вечное!",
                R.raw.stikhira_1_notes,
                R.raw.tropar_1_general,

                R.raw.stikhira_1_bass,
                R.raw.stikhira_1_bariton,
                R.raw.stikhira_1_tenor,
                R.raw.stikhira_1_soprano
        );
        tropary[1] = tropar2;

        Song tropar3 = createSong(
                3,
                "Ныне всё исполнилось светом: / небо, и земля, и преисподняя. / " +
                        "Пусть же празднует всё творение / Воскресение Христа, // Коим оно и утверждается.",
                R.raw.stikhira_1_notes,
                R.raw.tropar_1_general,

                R.raw.stikhira_1_bass,
                R.raw.stikhira_1_bariton,
                R.raw.stikhira_1_tenor,
                R.raw.stikhira_1_soprano
        );
        tropary[2] = tropar3;

        Song tropar4 = createSong(
                4,
                "Вчера меня с Тобою погребали, Христе, — / сегодня восстаю с Тобой, воскресшим;/" +
                        " вчера меня с Тобою распинали — / прославь же и меня с Собой, Спаситель, // когда Ты придёшь царствовать.",
                R.raw.stikhira_1_notes,
                R.raw.tropar_1_general,

                R.raw.stikhira_1_bass,
                R.raw.stikhira_1_bariton,
                R.raw.stikhira_1_tenor,
                R.raw.stikhira_1_soprano
        );
        tropary[3] = tropar4;

        Song tropar5 = createSong(
                5,
                "Первенцем Девы явился Христос. / Будучи человеком, / [Он] Агнцем именуется, / а непорочным — " +
                        "/ как не имеющий пятна. / Он — наша Пасха, / и как Бог истинный // назван совершенным.",
                R.raw.stikhira_1_notes,
                R.raw.tropar_1_general,

                R.raw.stikhira_1_bass,
                R.raw.stikhira_1_bariton,
                R.raw.stikhira_1_tenor,
                R.raw.stikhira_1_soprano
        );
        tropary[4] = tropar5;

        Song tropar6 = createSong(
                6,
                "Словно однолетний ягнёнок, / Христос за всех был за́клан по воле Своей: / Он — Пасха очищающая, " +
                        "/ Он нам благословенный Венец! / И снова из гроба прекрасное // Правды Солнце нам воссияло.",
                R.raw.stikhira_1_notes,
                R.raw.tropar_1_general,

                R.raw.stikhira_1_bass,
                R.raw.stikhira_1_bariton,
                R.raw.stikhira_1_tenor,
                R.raw.stikhira_1_soprano
        );
        tropary[5] = tropar6;

        Song tropar7 = createSong(
                7,
                "Пра́отец Давид / пред сенью ковчега скакал и играл, / мы же, как святой народ Божий, / " +
                        "видя исполнение зна́мений, / да возрадуемся в Духе, / ибо восстал Христос // как всесильный.",
                R.raw.stikhira_1_notes,
                R.raw.tropar_1_general,

                R.raw.stikhira_1_bass,
                R.raw.stikhira_1_bariton,
                R.raw.stikhira_1_tenor,
                R.raw.stikhira_1_soprano
        );
        tropary[6] = tropar7;

        Song tropar8 = createSong(
                8,
                "Пу́тами ада уде́рживаемые, / увидев Твоё, Христе, безмерное благосердие, / " +
                        "мы устремились к Свету весёлыми ногами, // прославляя Пасху вечную.",
                R.raw.stikhira_1_notes,
                R.raw.tropar_1_general,

                R.raw.stikhira_1_bass,
                R.raw.stikhira_1_bariton,
                R.raw.stikhira_1_tenor,
                R.raw.stikhira_1_soprano
        );
        tropary[7] = tropar8;


        SongsList stikhiryList = new SongsList(R.string.stikhira_glas_s, stikhiry);
        SongsList troparyList = new SongsList(R.string.tropar_glas_s, tropary);
        songsLists.add(troparyList);
        songsLists.add(stikhiryList);
    }

    public SongsList getSongsList(int id) {
        return songsLists.get(id);
    }

    public static Lab getLab() {
        if (lab == null)
            lab = new Lab();
        return lab;
    };

    @NotNull
    private Song createSong(int number, String text, int notesId, int generalSongId, int... toneSongsId) {
        Song song = new Song(id);
        id++;
        song.setNumber(number);
        song.setText(text);
        song.setGeneralSongId(generalSongId);
        song.setNotesId(notesId);
        song.addToneSongsId(toneSongsId);
        return song;
    }
}