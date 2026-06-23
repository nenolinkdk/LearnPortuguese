package dk.nenolink.learnportuguese;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends Activity {
    private final Phrase[] phrases = new Phrase[] {
            new Phrase("Olá, bom dia.", "Hej, godmorgen.", "Use bom dia before lunch.", "olá = hej, bom = god, dia = dag"),
            new Phrase("Eu sou da Dinamarca.", "Jeg er fra Danmark.", "Eu sou means I am.", "eu = jeg, sou = er, Dinamarca = Danmark"),
            new Phrase("Como te chamas?", "Hvad hedder du?", "Questions often keep the same word order as statements.", "como = hvordan, te chamas = hedder du"),
            new Phrase("Chamo-me Anna.", "Jeg hedder Anna.", "Chamo-me is a simple way to say my name is.", "chamo-me = jeg hedder"),
            new Phrase("Por favor, fala devagar.", "Vær sød at tale langsomt.", "Fala is the informal command for speak.", "por favor = vær sød, devagar = langsomt"),
            new Phrase("Não percebo.", "Jeg forstår ikke.", "Não goes before the verb to make a negative sentence.", "não = ikke, percebo = forstår"),
            new Phrase("Onde fica a estação?", "Hvor ligger stationen?", "Onde asks where.", "onde = hvor, fica = ligger, estação = station"),
            new Phrase("Quero um café.", "Jeg vil gerne have en kaffe.", "Quero means I want and is useful in cafés and shops.", "quero = jeg vil have, um = en, café = kaffe"),
            new Phrase("A água é boa.", "Vandet er godt.", "A and o often mean the.", "água = vand, é = er, boa = god"),
            new Phrase("Tenho fome.", "Jeg er sulten.", "Portuguese uses have hunger instead of am hungry.", "tenho = jeg har, fome = sult"),
            new Phrase("Tenho sede.", "Jeg er tørstig.", "This also uses tenho, meaning I have.", "sede = tørst"),
            new Phrase("A conta, por favor.", "Regningen, tak.", "Short polite phrases work well in restaurants.", "conta = regning, por favor = tak"),
            new Phrase("Quanto custa?", "Hvad koster det?", "Quanto asks how much.", "quanto = hvor meget, custa = koster"),
            new Phrase("Gosto de música.", "Jeg kan lide musik.", "Gosto de means I like.", "gosto de = jeg kan lide, música = musik"),
            new Phrase("Moro em Copenhaga.", "Jeg bor i København.", "Em means in or at.", "moro = jeg bor, em = i, Copenhaga = København"),
            new Phrase("Hoje está sol.", "I dag er der sol.", "Está describes temporary states like weather.", "hoje = i dag, sol = sol"),
            new Phrase("Amanhã vou estudar.", "I morgen vil jeg studere.", "Vou plus verb makes a simple future.", "amanhã = i morgen, vou = jeg går/vil, estudar = studere"),
            new Phrase("Preciso de ajuda.", "Jeg har brug for hjælp.", "Preciso de means I need.", "preciso de = jeg har brug for, ajuda = hjælp"),
            new Phrase("Até logo.", "Vi ses senere.", "Até means until and is used in goodbyes.", "até logo = vi ses senere"),
            new Phrase("Obrigado pela ajuda.", "Tak for hjælpen.", "Obrigado is said by a male speaker; obrigada by a female speaker.", "obrigado = tak, ajuda = hjælp")
    };

    private int index = 0;
    private TextToSpeech textToSpeech;
    private TextView counterView;
    private TextView portugueseView;
    private TextView danishView;
    private TextView grammarView;
    private TextView glossaryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSpeech();
        setContentView(buildLayout());
        showPhrase(0);
    }

    private void setupSpeech() {
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(new Locale("pt", "PT"));
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "Portuguese voice not installed on this Android device.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private View buildLayout() {
        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(dp(20), dp(24), dp(20), dp(24));
        root.setBackgroundColor(0xFFF5F2EA);
        scrollView.addView(root, new ScrollView.LayoutParams(
                ScrollView.LayoutParams.MATCH_PARENT,
                ScrollView.LayoutParams.WRAP_CONTENT
        ));

        TextView title = text("Learn Portuguese", 28, 0xFF173D35, true);
        root.addView(title);

        TextView aiBadge = text("AI used: beginner phrase set and grammar notes", 13, 0xFF36554C, false);
        aiBadge.setGravity(Gravity.CENTER);
        aiBadge.setPadding(dp(10), dp(8), dp(10), dp(8));
        aiBadge.setBackgroundColor(0xFFE0EFE9);
        root.addView(aiBadge, matchWrap());

        counterView = text("", 14, 0xFF5D665F, false);
        counterView.setPadding(0, dp(18), 0, dp(8));
        root.addView(counterView);

        portugueseView = text("", 30, 0xFF101A17, true);
        portugueseView.setGravity(Gravity.CENTER);
        root.addView(portugueseView, matchWrap());

        danishView = text("", 20, 0xFF33413C, false);
        danishView.setGravity(Gravity.CENTER);
        danishView.setPadding(0, dp(8), 0, dp(18));
        root.addView(danishView, matchWrap());

        Button speakButton = button("Pronounce Portuguese");
        speakButton.setOnClickListener(v -> speakCurrentPhrase());
        root.addView(speakButton, matchWrap());

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER);
        row.setPadding(0, dp(16), 0, dp(16));
        root.addView(row, matchWrap());

        Button previousButton = button("Previous");
        previousButton.setOnClickListener(v -> showPhrase(index - 1));
        row.addView(previousButton, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        Button nextButton = button("Next");
        nextButton.setOnClickListener(v -> showPhrase(index + 1));
        LinearLayout.LayoutParams nextParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        nextParams.setMargins(dp(12), 0, 0, 0);
        row.addView(nextButton, nextParams);

        grammarView = panel("Grammar");
        root.addView(grammarView, matchWrap());

        glossaryView = panel("Glossary");
        root.addView(glossaryView, matchWrap());

        return scrollView;
    }

    private void showPhrase(int newIndex) {
        if (newIndex < 0) {
            index = phrases.length - 1;
        } else if (newIndex >= phrases.length) {
            index = 0;
        } else {
            index = newIndex;
        }

        Phrase phrase = phrases[index];
        counterView.setText("Phrase " + (index + 1) + " of " + phrases.length + " · basic beginner words");
        portugueseView.setText(phrase.portuguese);
        danishView.setText(phrase.danish);
        grammarView.setText("Grammar\n" + phrase.grammar);
        glossaryView.setText("Glossary\n" + phrase.glossary);
    }

    private void speakCurrentPhrase() {
        if (textToSpeech != null) {
            textToSpeech.speak(phrases[index].portuguese, TextToSpeech.QUEUE_FLUSH, null, "phrase-" + index);
        }
    }

    private TextView text(String content, int sp, int color, boolean bold) {
        TextView view = new TextView(this);
        view.setText(content);
        view.setTextSize(sp);
        view.setTextColor(color);
        if (bold) {
            view.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
        }
        return view;
    }

    private TextView panel(String title) {
        TextView view = text(title, 17, 0xFF173D35, false);
        view.setPadding(dp(14), dp(12), dp(14), dp(12));
        view.setBackgroundColor(0xFFFFFFFF);
        return view;
    }

    private Button button(String label) {
        Button button = new Button(this);
        button.setText(label);
        button.setAllCaps(false);
        return button;
    }

    private LinearLayout.LayoutParams matchWrap() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, dp(6), 0, dp(6));
        return params;
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    private static class Phrase {
        final String portuguese;
        final String danish;
        final String grammar;
        final String glossary;

        Phrase(String portuguese, String danish, String grammar, String glossary) {
            this.portuguese = portuguese;
            this.danish = danish;
            this.grammar = grammar;
            this.glossary = glossary;
        }
    }
}
