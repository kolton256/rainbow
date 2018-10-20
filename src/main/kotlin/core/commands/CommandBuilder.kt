package core.commands

class CommandBuilder {
    private val command: Command

    init {
        command = object : Command {
            override fun toString(): String =
                    StringBuilder(this.name)
                            .append(" (")
                            .append(this.parameters.map {
                                if (!it.isOptional) "${it.name} : ${it.type!!.simpleName}"   //some : Int
                                else "<${it.name} : ${it.type!!.simpleName}>"                //<some : Int>
                            }
                                    .joinToString { it })
                            .append(")")
                            .toString()

            override fun execute(context: CommandContext) {
                action!!(context)
            }

            override var aliases: Array<String> = arrayOf()
            override var summary = "Описание отстутствует"
            override var name = ""
            override var parameters: Array<out ParamInfo> = arrayOf()
        }
    }

    var action: ((CommandContext) -> Unit)? = null

    var aliases: Array<String> = arrayOf()
        set(value) {
            command.aliases = value
        }

    var summary: String = ""
        set(value) {
            command.summary = value
        }

    fun parameters(vararg params: ParamBuilder.() -> ParamInfo) {
        command.parameters = params.map { it.invoke(ParamBuilder()) }.toTypedArray()
    }

    var name: String = ""
        set(value) {
            command.name = value
        }

    fun build(): Command {
        if (command.name == "")
            throw NullPointerException("Отсутствует имя команды")
        if (action == null)
            throw NullPointerException("Отсутствует действие, выполняемое командой")
        if (command.summary == "Описание отстутствует" || command.summary == "")
            println("Отсутствует описание для функции '${command.name}'")

        return command
    }
}