import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Heuristic that analyzes text by checking how many words match a dictionary of common English words
 * Higher scores indicate more dictionary matches
 */
public class DictionaryHeuristic implements Heuristic {
    
    // Basic English dictionary (subset of common words)
    private static final Set<String> DICTIONARY = new HashSet<>();
    static {
        // Common English words - this would ideally be loaded from the Extended Basic English wordlist
        String[] words = {
            "a", "about", "all", "also", "and", "as", "at", "be", "because", "but", "by", "can",
            "come", "could", "day", "do", "even", "find", "first", "for", "from", "get", "give",
            "go", "have", "he", "her", "here", "him", "his", "how", "i", "if", "in", "into", "is",
            "it", "its", "just", "know", "like", "look", "make", "man", "many", "me", "more", "my",
            "new", "no", "not", "now", "of", "on", "one", "only", "or", "other", "our", "out",
            "over", "say", "see", "she", "so", "some", "take", "than", "that", "the", "their",
            "them", "there", "these", "they", "think", "this", "time", "to", "two", "up", "use",
            "very", "want", "water", "way", "we", "well", "were", "what", "when", "which", "who",
            "will", "with", "would", "write", "you", "your", "people", "may", "down", "been",
            "call", "who", "oil", "sit", "set", "had", "let", "must", "big", "high", "such", "follow",
            "act", "why", "ask", "men", "change", "went", "light", "kind", "off", "need", "house",
            "picture", "try", "us", "again", "animal", "point", "mother", "world", "near", "build",
            "self", "earth", "father", "head", "stand", "own", "page", "should", "country", "found",
            "answer", "school", "grow", "study", "still", "learn", "plant", "cover", "food", "sun",
            "four", "between", "state", "keep", "eye", "never", "last", "let", "thought", "city",
            "tree", "cross", "farm", "hard", "start", "might", "story", "saw", "far", "sea", "draw",
            "left", "late", "run", "dont", "while", "press", "close", "night", "real", "life",
            "few", "north", "book", "carry", "took", "science", "eat", "room", "friend", "began",
            "idea", "fish", "mountain", "stop", "once", "base", "hear", "horse", "cut", "sure",
            "watch", "color", "face", "wood", "main", "enough", "plain", "girl", "usual", "young",
            "ready", "above", "ever", "red", "list", "though", "feel", "talk", "bird", "soon",
            "body", "dog", "family", "direct", "leave", "song", "measure", "door", "product", "black",
            "short", "numeral", "class", "wind", "question", "happen", "complete", "ship", "area",
            "half", "rock", "order", "fire", "south", "problem", "piece", "told", "knew", "pass",
            "since", "top", "whole", "king", "space", "heard", "best", "hour", "better", "during",
            "hundred", "five", "remember", "step", "early", "hold", "west", "ground", "interest",
            "reach", "fast", "verb", "sing", "listen", "six", "table", "travel", "less", "morning",
            "ten", "simple", "several", "vowel", "toward", "war", "lay", "against", "pattern",
            "slow", "center", "love", "person", "money", "serve", "appear", "road", "map", "rain",
            "rule", "govern", "pull", "cold", "notice", "voice", "unit", "power", "town", "fine",
            "certain", "fly", "fall", "lead", "cry", "dark", "machine", "note", "wait", "plan",
            "figure", "star", "box", "noun", "field", "rest", "correct", "able", "pound", "done",
            "beauty", "drive", "stood", "contain", "front", "teach", "week", "final", "gave",
            "green", "oh", "quick", "develop", "ocean", "warm", "free", "minute", "strong",
            "special", "mind", "behind", "clear", "tail", "produce", "fact", "street", "inch",
            "multiply", "nothing", "course", "stay", "wheel", "full", "force", "blue", "object",
            "decide", "surface", "deep", "moon", "island", "foot", "system", "busy", "test",
            "record", "boat", "common", "gold", "possible", "plane", "stead", "dry", "wonder",
            "laugh", "thousands", "ago", "ran", "check", "game", "shape", "equate", "miss",
            "brought", "heat", "snow", "tire", "bring", "yes", "distant", "fill", "east", "paint",
            "language", "among", "grand", "ball", "yet", "wave", "drop", "heart", "am", "present",
            "heavy", "dance", "engine", "position", "arm", "wide", "sail", "material", "size",
            "vary", "settle", "speak", "weight", "general", "ice", "matter", "circle", "pair",
            "include", "divide", "syllable", "felt", "perhaps", "pick", "sudden", "count", "square",
            "reason", "length", "represent", "art", "subject", "region", "energy", "hunt",
            "probable", "bed", "brother", "egg", "ride", "cell", "believe", "fraction", "forest",
            "sit", "race", "window", "store", "summer", "train", "sleep", "prove", "lone", "leg",
            "exercise", "wall", "catch", "mount", "wish", "sky", "board", "joy", "winter", "sat",
            "written", "wild", "instrument", "kept", "glass", "grass", "cow", "job", "edge", "sign",
            "visit", "past", "soft", "fun", "bright", "gas", "weather", "month", "million", "bear",
            "finish", "happy", "hope", "flower", "clothe", "strange", "gone", "jump", "baby", "eight",
            "village", "meet", "root", "buy", "raise", "solve", "metal", "whether", "push", "seven",
            "paragraph", "third", "shall", "held", "hair", "describe", "cook", "floor", "either",
            "result", "burn", "hill", "safe", "cat", "century", "consider", "type", "law", "bit",
            "coast", "copy", "phrase", "silent", "tall", "sand", "soil", "roll", "temperature",
            "finger", "industry", "value", "fight", "lie", "beat", "excite", "natural", "view",
            "sense", "ear", "else", "quite", "broke", "case", "middle", "kill", "son", "lake",
            "moment", "scale", "loud", "spring", "observe", "child", "straight", "consonant",
            "nation", "dictionary", "milk", "speed", "method", "organ", "pay", "age", "section",
            "dress", "cloud", "surprise", "quiet", "stone", "tiny", "climb", "bad", "oil", "blood",
            "touch", "grew", "cent", "mix", "team", "wire", "cost", "lost", "brown", "wear", "garden",
            "equal", "sent", "choose", "fell", "fit", "flow", "fair", "bank", "collect", "save",
            "control", "decimal", "gentle", "woman", "captain", "practice", "separate", "difficult",
            "doctor", "please", "protect", "noon", "whose", "locate", "ring", "character", "insect",
            "caught", "period", "indicate", "radio", "spoke", "atom", "human", "history", "effect",
            "electric", "expect", "crop", "modern", "element", "hit", "student", "corner", "party",
            "supply", "bone", "rail", "imagine", "provide", "agree", "thus", "capital", "wont",
            "chair", "danger", "fruit", "rich", "thick", "soldier", "process", "operate", "guess",
            "necessary", "sharp", "wing", "create", "neighbor", "wash", "bat", "rather", "crowd",
            "corn", "compare", "poem", "string", "bell", "depend", "meat", "rub", "tube", "famous",
            "dollar", "stream", "fear", "sight", "thin", "triangle", "planet", "hurry", "chief",
            "colony", "clock", "mine", "tie", "enter", "major", "fresh", "search", "send", "yellow",
            "gun", "allow", "print", "dead", "spot", "desert", "suit", "current", "lift", "rose",
            "continue", "block", "chart", "hat", "sell", "success", "company", "subtract", "event",
            "particular", "deal", "swim", "term", "opposite", "wife", "shoe", "shoulder", "spread",
            "arrange", "camp", "invent", "cotton", "born", "determine", "quart", "nine", "truck",
            "noise", "level", "chance", "gather", "shop", "stretch", "throw", "shine", "property",
            "column", "molecule", "select", "wrong", "gray", "repeat", "require", "broad", "prepare",
            "salt", "nose", "plural", "anger", "claim", "continent", "oxygen", "sugar", "death",
            "pretty", "skill", "women", "season", "solution", "magnet", "silver", "thank", "branch",
            "match", "suffix", "especially", "fig", "afraid", "huge", "sister", "steel", "discuss",
            "forward", "similar", "guide", "experience", "score", "apple", "bought", "led", "pitch",
            "coat", "mass", "card", "band", "rope", "slip", "win", "dream", "evening", "condition",
            "feed", "tool", "total", "basic", "smell", "valley", "nor", "double", "seat", "arrive",
            "master", "track", "parent", "shore", "division", "sheet", "substance", "favor",
            "connect", "post", "spend", "chord", "fat", "glad", "original", "share", "station",
            "dad", "bread", "charge", "proper", "bar", "offer", "segment", "slave", "duck",
            "instant", "market", "degree", "populate", "chick", "dear", "enemy", "reply", "drink",
            "occur", "support", "speech", "nature", "range", "steam", "motion", "path", "liquid",
            "log", "meant", "quotient", "teeth", "shell", "neck"
        };
        
        for (String word : words) {
            DICTIONARY.add(word.toLowerCase());
        }
    }
    
    private String lastSummary = "";
    
    @Override
    public double analyze(String text) {
        if (text == null || text.trim().isEmpty()) {
            lastSummary = "No text to analyze";
            return 0.0;
        }
        
        // Split text into words, removing punctuation
        String cleanText = text.toLowerCase().replaceAll("[^a-zA-Z\\s]", "");
        StringTokenizer tokenizer = new StringTokenizer(cleanText);
        
        int totalWords = 0;
        int validWords = 0;
        
        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken().trim();
            if (!word.isEmpty()) {
                totalWords++;
                if (DICTIONARY.contains(word)) {
                    validWords++;
                }
            }
        }
        
        if (totalWords == 0) {
            lastSummary = "No words found in text";
            return 0.0;
        }
        
        double score = (double) validWords / totalWords;
        lastSummary = String.format("Found %d/%d dictionary words (%.1f%%)", 
                                   validWords, totalWords, score * 100);
        
        return score;
    }
    
    @Override
    public String getName() {
        return "Dictionary Matching";
    }
    
    @Override
    public String getSummary() {
        return lastSummary;
    }
}