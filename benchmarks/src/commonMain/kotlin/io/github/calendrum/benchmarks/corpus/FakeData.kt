package io.github.calendrum.benchmarks.corpus

internal object FakeData {
    val latinFirstNames: List<String> = listOf(
        "Alice", "Bob", "Carol", "David", "Emma", "Frank", "Grace", "Henry",
        "Isla", "Jack", "Karen", "Liam", "Mia", "Noah", "Olivia", "Peter",
        "Quinn", "Ruth", "Simon", "Tara", "Uma", "Victor", "Wendy", "Xavier",
    )

    val latinLastNames: List<String> = listOf(
        "Anderson", "Baker", "Clarke", "Dawson", "Ellis", "Fischer", "Garcia",
        "Harris", "Ingram", "Johnson", "Kowalski", "Lindberg", "Moreau", "Nilsen",
        "O'Brien", "Petrov", "Quinlan", "Reyes", "Sorensen", "Turner", "Ueda", "Vance",
    )

    val cyrillicFirstNames: List<String> = listOf(
        "Александра", "Борис", "Вера", "Григорий", "Дарья", "Егор", "Жанна",
        "Зинаида", "Иван", "Ксения", "Леонид", "Мария", "Никита", "Ольга",
        "Павел", "Роман", "Светлана", "Тимофей", "Ульяна", "Фёдор",
    )

    val cyrillicLastNames: List<String> = listOf(
        "Иванов", "Петрова", "Сидоров", "Кузнецова", "Смирнов", "Попова",
        "Волков", "Соколова", "Морозов", "Лебедева", "Новиков", "Козлова",
        "Григорьев", "Орлова", "Романов", "Фролова",
    )

    val domains: List<String> = listOf("example.com", "example.org", "example.net")

    val roles: List<String> = listOf("REQ-PARTICIPANT", "OPT-PARTICIPANT", "NON-PARTICIPANT", "CHAIR")

    val partstats: List<String> = listOf("NEEDS-ACTION", "ACCEPTED", "DECLINED", "TENTATIVE")

    val meetingTopics: List<String> = listOf(
        "Sprint Planning", "Budget Review", "Architecture Sync", "Onboarding",
        "Retrospective", "Design Review", "Roadmap Discussion", "Customer Call",
        "Совещание отдела", "Обсуждение проекта", "Планёрка", "Ревью архитектуры",
    )

    val locations: List<String> = listOf(
        "Room 204, Building A",
        "Conference Hall, 3rd floor",
        "Zoom",
        "Каб. 314, Главный корпус",
        "Актовый зал",
        "Онлайн",
    )

    val categories: List<String> = listOf(
        "MEETING",
        "WORK",
        "PERSONAL",
        "TRAVEL",
        "EDUCATION",
        "HOLIDAY",
        "IMPORTANT",
    )

    val secondaryCategories: List<String> = categories.filter { it != "EDUCATION" }

    val descriptionFragments: List<String> = listOf(
        "Please review the attached agenda before joining.",
        "Bring your laptop and the latest report — numbers may change.",
        "Не забудьте распечатать материалы — они понадобятся на месте 📄.",
        "Обсудим статус задач и риски на следующий спринт 🚀.",
        "Reminder: this event repeats weekly until the end of the term.",
        "Léquipe présentera les résultats du trimestre; café offert ☕.",
        "Пожалуйста, подтвердите участие; вопросы — в чат 💬.",
        "Note: rooms are subject to change; check the board on arrival.",
        "場所は変更の可能性があります。確認をお願いします。",
        "Agenda: intros; status; open questions; next steps; wrap-up 📝.",
    )

    val courseNames: List<String> = listOf(
        "Calculus I", "Linear Algebra", "Data Structures", "Operating Systems",
        "Discrete Mathematics", "Physics I", "Database Systems", "Compilers",
        "Machine Learning", "Computer Networks", "Software Engineering", "Algorithms",
        "Введение в программирование", "Дискретная математика", "Базы данных",
        "Операционные системы", "Компьютерные сети", "Матанализ",
    )

    val courseSections: List<String> = listOf("A", "B", "C", "D", "E", "F", "G", "H")

    fun latinFullName(rng: SplitMix64): String = "${rng.pick(latinFirstNames)} ${rng.pick(latinLastNames)}"

    fun cyrillicFullName(rng: SplitMix64): String = "${rng.pick(cyrillicFirstNames)} ${rng.pick(cyrillicLastNames)}"

    fun personName(rng: SplitMix64): String = if (rng.chancePercent(50)) latinFullName(rng) else cyrillicFullName(rng)

    fun emailLocalPart(name: String, index: Int): String {
        val transliterated = name.lowercase().replace(' ', '.').filter { it.isLetter() || it == '.' }
        val ascii = transliterated.map { ch -> LATIN_FALLBACK[ch] ?: ch }.joinToString("")
        return "$ascii$index"
    }

    fun mailto(name: String, index: Int, rng: SplitMix64): String =
        "mailto:${emailLocalPart(name, index)}@${rng.pick(domains)}"

    fun description(rng: SplitMix64, sentences: Int): String =
        (1..sentences).joinToString(" ") { rng.pick(descriptionFragments) }

    private val LATIN_FALLBACK: Map<Char, Char> = buildMap {
        val cyrillic = "абвгдежзийклмнопрстуфхцчшщъыьэюяё"
        val latin = "abvgdejzijklmnoprstufhcCssyyeuaje"
        cyrillic.forEachIndexed { index, ch -> put(ch, latin.getOrElse(index) { 'x' }) }
    }
}
