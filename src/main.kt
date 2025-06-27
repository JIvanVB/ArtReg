import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File
import java.io.FileWriter
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.SwingUtilities
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.system.exitProcess

val lista = HashSet<String>()
var ruta : String = ""
val display=Display()
var titulo = JLabel("Sin archivo").apply { foreground = Color(238, 238, 238)}

fun main(){
    SwingUtilities.invokeLater {
        JFrame().apply {
            setSize(640, 480)
            isUndecorated=true
            background = Color(0, 0, 0, 0)
            defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            setLocationRelativeTo(null)
            add(PanelRound())
            isVisible = true
        }
    }
}

class PanelRound(var fondo : Color = Color(34, 40, 49),var arc:Int = 35 , var padding:Int = 20):JPanel(){

    init {
        background= Color(0,0,0,0)
        isOpaque=false
        layout= BorderLayout()
        border = javax.swing.BorderFactory.createEmptyBorder(padding, padding, padding, padding)
        add(Nav(), BorderLayout.NORTH)
        add(PaddingDisplay(), BorderLayout.CENTER)
        add(Input(), BorderLayout.SOUTH)

    }
    override fun paintComponent(g: Graphics?) {
        (g!!.create() as Graphics2D).apply {
            setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            color = fondo
            fillRoundRect(0,0,width,height,arc,arc)
        }
    }
}

class PaddingDisplay(var padding:Int = 20):JPanel(){

    init {
        isOpaque=false
        background= Color(0,0,0,0)
        border = javax.swing.BorderFactory.createEmptyBorder(padding, 0, padding, 0)
        layout= BorderLayout()
        add(Escroll(), BorderLayout.CENTER)

    }
}

class Escroll: JScrollPane(){

    init {
        border = null
        isOpaque=false
        background=Color(0,0,0,0)
        viewport.isOpaque = false
        viewport.background = Color(0, 0, 0, 0)
        setViewportView(display)
        horizontalScrollBarPolicy = HORIZONTAL_SCROLLBAR_AS_NEEDED
        verticalScrollBarPolicy = VERTICAL_SCROLLBAR_AS_NEEDED

    }
}

class Display(var padding:Int=20): JTextArea(){
    init {
        font= Font(this.font.name, Font.PLAIN, 14)
        foreground=Color(238, 238, 238)
        isEditable=false
        border = javax.swing.BorderFactory.createEmptyBorder(padding, padding, padding, padding)
        isOpaque = false
        addMouseListener(object : MouseAdapter(){
            override fun mousePressed(e: MouseEvent?) {
                lista.clear()
                JFileChooser().apply {
                    currentDirectory = File(System.getProperty("user.dir"))
                    fileFilter = FileNameExtensionFilter("Archivos de texto (*.txt)", "txt")
                    if (showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                        selectedFile.forEachLine { lista.add(it) }
                    ruta=selectedFile.path
                    titulo.text=ruta
                }

                repeat(lista.size) { text = lista.joinToString("\n") }
            }
        })
        lineWrap=false
    }

    override fun paintComponent(g: Graphics?) {
        (g!!.create() as Graphics2D).apply {
            setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            color=Color(49, 54, 63)
            fillRoundRect(0,0,width,height,35,35)
            color=foreground
            if(text.isEmpty())
                drawString("Has click para abrir un archivo",width/4,height/2)
        }
        super.paintComponent(g)
    }
}

class Input(var padding:Int=20): JTextField(){
    init {
        foreground=Color(238, 238, 238)
        preferredSize = Dimension(40, 40)
        maximumSize = Dimension(40, 40)
        minimumSize = Dimension(40, 40)
        border = javax.swing.BorderFactory.createEmptyBorder(0, padding, 0, padding)
        isOpaque = false
        addActionListener {
            lista.add(text)
            repeat(lista.size) { display.text = lista.joinToString("\n") }
            text=""
        }
    }


    override fun paintComponent(g: Graphics?) {
        (g!!.create() as Graphics2D).apply {
            setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            color=Color(118, 171, 174)
            fillRoundRect(0,0,width,height,35,35)
        }
        super.paintComponent(g)

    }

}

class Nav(var padding: Int = 20):JPanel(){

    init {
        isOpaque=false
        preferredSize = Dimension(width, 40)
        maximumSize = Dimension(width, 40)
        layout = BorderLayout()
        border = javax.swing.BorderFactory.createEmptyBorder(0, padding, 0, padding)
        add(Box.createVerticalBox().apply {
            add(Box.createVerticalGlue())
            add(Box(BoxLayout.X_AXIS).apply {
                add(Guardar("save"))
                add(this.add(Box.createHorizontalStrut(10)))
                add(Cerrar("x"))
            })
            add(Box.createVerticalGlue())
        }, BorderLayout.EAST)

        add(Box.createVerticalBox().apply {
            add(Box.createVerticalGlue())
            add(Box(BoxLayout.X_AXIS).apply {add(titulo)})
            add(Box.createVerticalGlue())
        },BorderLayout.WEST)

    }

    override fun paintComponent(g: Graphics?) {
        (g!!.create() as Graphics2D).apply {
            color=Color(0,0,0,0)
            fillRoundRect(0,0,width,height,35,35)
            dispose()
        }
    }
}

class Guardar(var texto: String = "", var fondo: Color = Color(49, 54, 63)) : JButton(texto) {

    init {
        preferredSize = Dimension(40, 40)
        maximumSize = Dimension(40, 40)
        minimumSize = Dimension(40, 40)
        isFocusPainted = false
        isBorderPainted = false
        border = null
        isContentAreaFilled = false
        isOpaque = false
        foreground = Color.white

        addActionListener {
            File(ruta).apply {
                writeText(lista.joinToString("\n"))
            }
        }

        addMouseListener(object : MouseAdapter() {
            override fun mouseEntered(e: MouseEvent?) {fondo = Color.green}
            override fun mouseExited(e: MouseEvent?) {fondo = Color(49, 54, 63)}
            override fun mousePressed(e: MouseEvent?) {fondo = Color.green.darker().darker()}
            override fun mouseReleased(e: MouseEvent?) {fondo = Color(49, 54, 63)}
        })
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        (g!!.create() as Graphics2D).apply {
            setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            color = fondo
            fillOval(0, 0, width, height)
            dispose()
        }
        super.paintComponent(g)
    }
}

class Cerrar(var texto: String = "", var fondo: Color = Color(49, 54, 63)) : JButton(texto) {

    init {
        preferredSize = Dimension(40, 40)
        maximumSize = Dimension(40, 40)
        minimumSize = Dimension(40, 40)
        isFocusPainted = false
        isBorderPainted = false
        border = null
        isContentAreaFilled = false
        isOpaque = false
        foreground = Color.white

        addActionListener { exitProcess(0) }

        addMouseListener(object : MouseAdapter() {
            override fun mouseEntered(e: MouseEvent?) {fondo = Color.red}
            override fun mouseExited(e: MouseEvent?) {fondo = Color(49, 54, 63)}
            override fun mousePressed(e: MouseEvent?) {fondo = Color.red.darker().darker()}
            override fun mouseReleased(e: MouseEvent?) {fondo = Color(49, 54, 63)}
        })
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        (g!!.create() as Graphics2D).apply {
            setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            color = fondo
            fillOval(0, 0, width, height)
            dispose()
        }
        super.paintComponent(g)
    }
}

