import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import java.awt.RenderingHints
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities
import kotlin.system.exitProcess

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

class PanelRound(var fondo : Color = Color(34, 40, 49),var arc:Int = 35 ):JPanel(){

    init {
        background= Color(0,0,0,0)
        isOpaque=false
        layout= BorderLayout()
        add(Nav(), BorderLayout.NORTH)

    }
    override fun paintComponent(g: Graphics?) {
        (g!!.create() as Graphics2D).apply {
            setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            color = fondo
            fillRoundRect(0,0,width,height,arc,arc)
        }
    }
}

class Nav(var padding: Int = 20):JPanel(){

    init {
        background= Color(0,0,0,0)
        isOpaque=false
        location= Point(padding,padding)
        preferredSize = Dimension(width-2*padding, 95)
        maximumSize = Dimension(width-2*padding, 95)

    }

    init {
        layout= FlowLayout(FlowLayout.RIGHT)
        add(Cerrar("X"))
    }
}

class Cerrar(var texto: String = "", var fondo: Color = Color(49, 54, 63)) : JButton(texto) {

    init {
        preferredSize = Dimension(40, 40)
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

