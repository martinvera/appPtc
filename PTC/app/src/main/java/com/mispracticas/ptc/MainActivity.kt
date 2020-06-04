package com.mispracticas.ptc

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.FieldClassification
import android.text.InputType.TYPE_CLASS_NUMBER
import android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isNotEmpty
import androidx.core.view.size
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Delayed
import kotlin.math.PI

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,
    CompoundButton.OnCheckedChangeListener {
    var contenedor: LinearLayout?=null
    var selecPulga: Spinner?=null
    var btnMás : Button?=null
    var btnMenos : Button?=null
    var cbKn: CheckBox?=null
    var btnNew: Button?=null
    var btnCalcular : Button?=null
    var diamIndice: Int=0
    var allLinLauTestigo= arrayListOf<LinearLayout>()
    var allCbs= arrayListOf<CheckBox>()
    var allTxA = arrayListOf<EditText>()
    var allTxD= arrayListOf<EditText>()
    var allTxC= arrayListOf<EditText>()
    var contenedorResultados: LinearLayout?=null
    var veces=0
    var colorTestigo=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contenedor=findViewById<LinearLayout>(R.id.contenedor)
        selecPulga=findViewById<Spinner>(R.id.spinnerSelectorDiametro)
        selecPulga!!.onItemSelectedListener=this
        btnMenos=findViewById<Button>(R.id.menosT)
        btnMenos!!.setOnClickListener {
            var jk=allLinLauTestigo.size
            if(diamIndice==0&&jk==2) {
                Toast.makeText(
                    applicationContext,
                    "ACI sugiere 2 testigos para"+"\n"+"           probetas de 6 in",
                    Toast.LENGTH_SHORT
                ).show()
                creadorDeTestigos(jk, 1)
            }
            else if(diamIndice==1&&jk==3){
                Toast.makeText(applicationContext, "ACI sugiere 3 testigos para"+"\n"+"           probetas de 4 in", Toast.LENGTH_SHORT).show()
                creadorDeTestigos(jk,1)
            }
            else if(diamIndice==1&&jk==2){
                Toast.makeText(applicationContext, "ACI sugiere 3 testigos para"+"\n"+"           probetas de 4 in", Toast.LENGTH_SHORT).show()
                creadorDeTestigos(jk,1)
            }
            else if(jk==1){
                Toast.makeText(applicationContext, "El mínimo de Testigos es 01", Toast.LENGTH_SHORT).show()
            }
            else{
                creadorDeTestigos(jk,1)}
        }
        btnMás=findViewById<Button>(R.id.másT)
        btnMás!!.setOnClickListener {
            var jk=allLinLauTestigo.size
            if(diamIndice==1&&jk==1){
                Toast.makeText(applicationContext, "ACI sugiere 3 testigos para"+"\n"+"           probetas de 4 in", Toast.LENGTH_SHORT).show()
                creadorDeTestigos(jk,2)
            }
            else if(jk==10){
                Toast.makeText(applicationContext, "No es posible crear más Testigos", Toast.LENGTH_SHORT).show()
            }
            else{
                creadorDeTestigos(jk,2)}
        }
        btnNew=findViewById<Button>(R.id.newCo)
        btnNew!!.setOnClickListener {
            creadorDeTestigos(allLinLauTestigo.size,0)
        }
        btnCalcular=findViewById<Button>(R.id.calcularT)
        btnCalcular!!.setOnClickListener {
            if(checar()==1){
                hacerCalculos()
            }
        }
        cbKn=findViewById<CheckBox>(R.id.cboKn)
        cbKn!!.setOnCheckedChangeListener(this)
        contenedorResultados = LinearLayout(applicationContext)
        contenedorResultados!!.orientation = LinearLayout.VERTICAL
        contenedorResultados!!.setBackgroundColor(Color.LTGRAY)

    }
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        diamIndice=position
        colorTestigo=0
        if(position==0){
            creadorDeTestigos(2,0)

            if (veces==0){
                contenedor!!.addView(contenedorResultados)
                var msjAdvtn=TextView(applicationContext)
                msjAdvtn.text="La velocidad de compresión debe ser 0.25 ±0.05 MPa/s"
                msjAdvtn.setTextColor(Color.WHITE)
                msjAdvtn.setBackgroundColor(Color.RED)
                msjAdvtn.gravity=Gravity.CENTER_HORIZONTAL
                msjAdvtn.setTextSize(16F)
                contenedorResultados!!.addView(msjAdvtn)
                veces++
            }


        }
        else{
            contenedorResultados!!.removeAllViews()
            contenedor!!.removeView(contenedorResultados)
            creadorDeTestigos(3,0)
        }
    }
    fun creadorDeTestigos(cantidad: Int, control:Int){
        if(control==0){
            contenedor!!.removeAllViews()
            cboKn!!.isChecked=false
            allLinLauTestigo.clear()
            allCbs.clear()
            allTxA.clear()
            allTxD.clear()
            allTxC.clear()
            for (x in 0 until cantidad) {
                val subConteneVertical = LinearLayout(applicationContext)
                subConteneVertical.id = x
                subConteneVertical.orientation = LinearLayout.VERTICAL
                if (colorTestigo==0){
                    subConteneVertical.setBackgroundColor(Color.parseColor("#191E88E5"))
                    colorTestigo=1
                }
                else if (colorTestigo==1){
                    subConteneVertical.setBackgroundColor(Color.WHITE)
                    colorTestigo=0
                }
                allLinLauTestigo.add(subConteneVertical)
                contenedor!!.addView(subConteneVertical)

                val lb = TextView(applicationContext)
                lb.text = "Testigo 0" + (x + 1)
                lb.id = x
                lb.height = 65
                lb.setTextColor(Color.BLUE)
                lb.setTextSize(17F)
                lb.gravity=Gravity.CENTER_HORIZONTAL
                subConteneVertical!!.addView(lb)

                val cb = CheckBox(applicationContext)
                cb.text = "Relación L/D mayor a 1.75 "
                cb.id = x
                cb.isChecked = true
                cb.setTextColor(Color.BLACK)
                cb.height = 80
                allCbs.add(cb)
                subConteneVertical!!.addView(cb)

                val txA = EditText(applicationContext)
                txA.hint = "Ingresa la Altura (mm)"
                txA.id = x
                txA.isSingleLine = true
                txA.setTextColor(Color.BLACK)
                txA.visibility = View.GONE
                txA.inputType = TYPE_CLASS_NUMBER or TYPE_NUMBER_FLAG_DECIMAL
                txA.height = 120
                allTxA.add(txA)
                txA.gravity=Gravity.CENTER_HORIZONTAL
                subConteneVertical!!.addView(txA)

                val txD = EditText(applicationContext)
                txD.hint = "Ingrese el Diámetro (mm)"
                txD.id = x
                txD.isSingleLine=true
                txD.inputType=  TYPE_CLASS_NUMBER or TYPE_NUMBER_FLAG_DECIMAL
                txD.setTextColor(Color.BLACK)
                txD.height=117
                allTxD.add(txD)
                txD.gravity=Gravity.CENTER_HORIZONTAL
                subConteneVertical!!.addView(txD)

                val txC = EditText(applicationContext)
                txC.hint = "Ingrese Carga (kg)"
                txC.id = x
                txC.isSingleLine=true
                txC.setTextColor(Color.BLACK)
                txC.inputType=  TYPE_CLASS_NUMBER or TYPE_NUMBER_FLAG_DECIMAL
                txC.height=117
                txC.width=500
                allTxC.add(txC)
                txC.gravity=Gravity.CENTER_HORIZONTAL
                subConteneVertical!!.addView(txC)

                val lbE = TextView(applicationContext)
                lbE.visibility=View.INVISIBLE
                lbE.height=50
                lbE.id=x
                subConteneVertical!!.addView(lbE)

                cb.setOnCheckedChangeListener { buttonView, isChecked ->
                    var nose=buttonView.id
                    if(!isChecked){
                        txA.visibility=View.VISIBLE
                        allCbs[nose].isChecked=false
                        txA.requestFocus()
                    }
                    else if(isChecked){
                        txA.visibility=View.GONE
                        allCbs[nose].isChecked=true
                        txD.requestFocus()
                    }
                }
            }
            allTxD[0].requestFocus()
        }
        else if(control==2){
            contenedorResultados!!.removeAllViews()
            val subConteneVertical = LinearLayout(applicationContext)
            subConteneVertical.id = cantidad
            subConteneVertical.orientation = LinearLayout.VERTICAL
            if (colorTestigo==0){
                subConteneVertical.setBackgroundColor(Color.parseColor("#191E88E5"))
                colorTestigo=1
            }
            else if (colorTestigo==1){
                subConteneVertical.setBackgroundColor(Color.WHITE)
                colorTestigo=0
            }
            allLinLauTestigo.add(subConteneVertical)
            contenedor!!.addView(subConteneVertical)

            val lb = TextView(applicationContext)
            lb.text = "Testigo 0"+ (cantidad+1)
            lb.id=cantidad
            lb.height=65
            lb.setTextColor(Color.BLUE)
            lb.setTextSize(17F)
            lb.gravity=Gravity.CENTER_HORIZONTAL
            subConteneVertical!!.addView(lb)

            val cb = CheckBox(applicationContext)
            cb.text = "Relación L/D mayor a 1.75 "
            cb.id = cantidad
            cb.isChecked=true
            cb.setTextColor(Color.BLACK)
            cb.height=80
            allCbs.add(cb)
            subConteneVertical!!.addView(cb)

            val txA = EditText(applicationContext)
            txA.hint = "Ingresa la Altura (mm)"
            txA.id = cantidad
            txA.isSingleLine=true
            txA.setTextColor(Color.BLACK)
            txA.visibility=View.GONE
            txA.inputType=  TYPE_CLASS_NUMBER or TYPE_NUMBER_FLAG_DECIMAL
            txA.height=120
            txA.gravity=Gravity.CENTER_HORIZONTAL
            allTxA.add(txA)
            subConteneVertical!!.addView(txA)

            val txD = EditText(applicationContext)
            txD.hint = "Ingrese el Diámetro (mm)"
            txD.id = cantidad
            txD.isSingleLine=true
            txD.inputType=  TYPE_CLASS_NUMBER or TYPE_NUMBER_FLAG_DECIMAL
            txD.setTextColor(Color.BLACK)
            txD.height=117
            txD.gravity=Gravity.CENTER_HORIZONTAL
            allTxD.add(txD)
            subConteneVertical!!.addView(txD)

            val txC = EditText(applicationContext)
            if(cbKn!!.isChecked){
                txC.hint = "Ingrese Carga (kN)"
            }else{txC.hint = "Ingrese Carga (kg)"}

            txC.id = cantidad
            txC.isSingleLine=true
            txC.setTextColor(Color.BLACK)
            txC.inputType=  TYPE_CLASS_NUMBER or TYPE_NUMBER_FLAG_DECIMAL
            txC.height=117
            txC.width=500
            txC.gravity=Gravity.CENTER_HORIZONTAL
            allTxC.add(txC)
            subConteneVertical!!.addView(txC)

            val lbE = TextView(applicationContext)
            lbE.visibility=View.INVISIBLE
            lbE.height=50
            lbE.id=cantidad
            subConteneVertical!!.addView(lbE)

            cb.setOnCheckedChangeListener { buttonView, isChecked ->
                var nose=buttonView.id
                if(!isChecked){
                    txA.visibility=View.VISIBLE
                    allCbs[nose].isChecked=false
                    txA.requestFocus()
                }
                else if(isChecked){
                    txA.visibility=View.GONE
                    allCbs[nose].isChecked=true
                    txD.requestFocus()
                }
            }

            allTxD[cantidad].requestFocus()

        }
        else if(control==1){
            if (colorTestigo==0){

                colorTestigo=1
            }
            else if (colorTestigo==1){

                colorTestigo=0
            }
            contenedorResultados!!.removeAllViews()
            contenedor!!.removeView(allLinLauTestigo[cantidad-1])
            allLinLauTestigo.removeAt(allLinLauTestigo[cantidad-1].id)
            allCbs.removeAt(allCbs[cantidad-1].id)
            allTxA.removeAt(allTxA[cantidad-1].id)
            allTxD.removeAt(allTxD[cantidad-1].id)
            allTxC.removeAt(allTxC[cantidad-1].id)
            allTxD[cantidad-2].requestFocus()
        }
    }
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {


        for (x in 0 until allLinLauTestigo.size) {
            if(isChecked) {
                allTxC[x].hint = "Ingrese Carga (kN)"
            }
            else{
                allTxC[x].hint = "Ingrese Carga (kg)"
            }
        }


    }
    fun checar(): Int {
        var rt =1
        var io=allLinLauTestigo.size-1
        while (io>=0){
            if (!allCbs[io].isChecked&&allTxA[io].text.toString()==""){
                var sr=io+1
                Toast.makeText(applicationContext, "Falta la altura del Testigo 0"+sr, Toast.LENGTH_SHORT).show()
                allTxA[io].requestFocus()
                rt=2
            }
            else if (allTxD[io].text.toString()==""){
                var sr=io+1
                Toast.makeText(applicationContext, "Falta el diámetro del Testigo 0"+sr, Toast.LENGTH_SHORT).show()
                allTxD[io].requestFocus()
                rt=2
            }
            else if (allTxD[io].text.toString().toDouble()<10){
                Toast.makeText(applicationContext, "El diámetro no puede ser menor a 10mm", Toast.LENGTH_SHORT).show()
                allTxD[io].requestFocus()
                rt=2
            }
            else if (allTxC[io].text.toString()==""){
                var sr=io+1
                Toast.makeText(applicationContext, "Falta la carga del Testigo 0"+sr, Toast.LENGTH_SHORT).show()
                allTxC[io].requestFocus()
                rt=2
            }
            io--
        }
        return rt
    }
    fun hacerCalculos(){
        val testiTotal=allLinLauTestigo.size
        var esfParcils= arrayListOf<Double>()

        contenedorResultados!!.removeAllViews()
        contenedor!!.removeView(contenedorResultados)
        contenedor!!.addView(contenedorResultados)

        var resTitulo=TextView(applicationContext)
        resTitulo.text="Resultado del Calculo"
        resTitulo.setTextColor(Color.BLUE)
        resTitulo.gravity=Gravity.CENTER_HORIZONTAL
        resTitulo.setTextSize(20F)
        contenedorResultados!!.addView(resTitulo)

        for (x in 0 until testiTotal){
            val di = allTxD[x].text.toString().toDouble()
            val ca = allTxC[x].text.toString().toDouble()

            var tituTest=TextView(applicationContext)
            tituTest.text="Testigo 0"+(x+1)
            tituTest.setTextColor(Color.BLUE)
            tituTest.gravity=Gravity.CENTER_HORIZONTAL
            tituTest.setTextSize(17F)
            contenedorResultados!!.addView(tituTest)

            if(!cbKn!!.isChecked){
                var areaCm2=(Math.round((((di*di* PI)/4)/100)*10)/(10.0))
                var calcParcialKg=Math.round(ca/areaCm2)


                if(!allCbs[x].isChecked){
                    val al=allTxA[x].text.toString().toDouble()
                    var ld=Math.round((al/di)*100)/100.0
                    var facCorrec=obtenerFactorCorreccion(ld,x)
                    if(facCorrec==-1.0){
                        esfParcils.add(calcParcialKg*1.0)

                        val m=TextView(applicationContext)
                        m.text="L/D="+ld+" mayor a 1.75"
                        m.gravity=Gravity.CENTER_HORIZONTAL
                        contenedorResultados!!.addView(m)

                        val q=TextView(applicationContext)
                        q.text="Esfuerzo =  "+calcParcialKg+" kg/cm²"+"\n"+"                   ("+Math.round(calcParcialKg*0.0980665*10)/10.0+"MPa aprox)"
                        q.setTextColor(Color.DKGRAY)
                        q.gravity=Gravity.CENTER_HORIZONTAL
                        q.setTextSize(15F)
                        contenedorResultados!!.addView(q)

                        val spaceX=TextView(applicationContext)
                        spaceX.text=""
                        spaceX.height=50
                        contenedorResultados!!.addView(spaceX)
                    }
                    else if(facCorrec==-2.0){
                        esfParcils.add(calcParcialKg*1.0)

                        val m=TextView(applicationContext)
                        m.text="L/D="+ld+" debajo de la norma"
                        m.gravity=Gravity.CENTER_HORIZONTAL
                        m.setTextColor(Color.RED)
                        contenedorResultados!!.addView(m)

                        val q=TextView(applicationContext)
                        q.text="Esfuerzo =  "+calcParcialKg+" Kg/cm²"+"\n"+"                   ("+Math.round(calcParcialKg*0.0980665*10)/10.0+"MPa aprox)"
                        q.setTextColor(Color.RED)
                        q.setTextSize(15F)
                        q.gravity=Gravity.CENTER_HORIZONTAL
                        contenedorResultados!!.addView(q)

                        val spaceX=TextView(applicationContext)
                        spaceX.text=""
                        spaceX.height=50
                        contenedorResultados!!.addView(spaceX)
                    }
                    else{

                        val q=TextView(applicationContext)
                        q.text="Esfuerzo s/corrección="+calcParcialKg+" kg/cm²"
                        q.gravity=Gravity.CENTER_HORIZONTAL
                        contenedorResultados!!.addView(q)

                        val h=TextView(applicationContext)
                        h.text="L/D="+ld+"  →  Factor="+facCorrec
                        h.gravity=Gravity.CENTER_HORIZONTAL
                        contenedorResultados!!.addView(h)

                        calcParcialKg=Math.round(calcParcialKg*facCorrec)


                        val i=TextView(applicationContext)
                        i.text="Esfuerzo =  "+calcParcialKg+" kg/cm²"+"\n"+"                   ("+Math.round(calcParcialKg*0.0980665*10)/10.0+"MPa aprox)"
                        i.setTextColor(Color.DKGRAY)
                        i.gravity=Gravity.CENTER_HORIZONTAL
                        i.setTextSize(15F)
                        contenedorResultados!!.addView(i)

                        esfParcils.add(calcParcialKg*1.0)

                        val spaceX=TextView(applicationContext)
                        spaceX.text=""
                        spaceX.height=50
                        contenedorResultados!!.addView(spaceX)
                    }
                }
                else{
                    esfParcils.add(calcParcialKg*1.0)

                    val q=TextView(applicationContext)
                    q.text="Esfuerzo =  "+calcParcialKg+" kg/cm²"+"\n"+"                   ("+Math.round(calcParcialKg*0.0980665*10)/10.0+"MPa aprox)"
                    q.setTextColor(Color.DKGRAY)
                    q.gravity=Gravity.CENTER_HORIZONTAL
                    q.setTextSize(15F)
                    contenedorResultados!!.addView(q)

                    val spaceX=TextView(applicationContext)
                    spaceX.text=""
                    spaceX.height=50
                    contenedorResultados!!.addView(spaceX)
                }


            }

            else if(cbKn!!.isChecked){
                var areaMm2=Math.round((di*di* PI)/4)
                var calcParcialKn= Math.round((ca/areaMm2)*10000)/10.0

                if(!allCbs[x].isChecked){
                    val al=allTxA[x].text.toString().toDouble()
                    var ld=Math.round((al/di)*100)/100.0
                    var facCorrec=obtenerFactorCorreccion(ld,x)
                    if(facCorrec==-1.0){
                        esfParcils.add(calcParcialKn)

                        val m=TextView(applicationContext)
                        m.text="L/D="+ld+" mayor a 1.75"
                        m.gravity=Gravity.CENTER_HORIZONTAL
                        contenedorResultados!!.addView(m)

                        val q=TextView(applicationContext)
                        q.text="Esfuerzo =  "+calcParcialKn+" MPa"+"\n"+"                   ("+Math.round(calcParcialKn*10.1972)+"kg/cm² aprox)"
                        q.setTextColor(Color.DKGRAY)
                        q.gravity=Gravity.CENTER_HORIZONTAL
                        q.setTextSize(15F)
                        contenedorResultados!!.addView(q)

                        val spaceX=TextView(applicationContext)
                        spaceX.text=""
                        spaceX.height=50
                        contenedorResultados!!.addView(spaceX)
                    }
                    else if(facCorrec==-2.0){
                        esfParcils.add(calcParcialKn)

                        val m=TextView(applicationContext)
                        m.text="L/D="+ld+" debajo de la norma"
                        m.gravity=Gravity.CENTER_HORIZONTAL
                        m.setTextColor(Color.RED)
                        contenedorResultados!!.addView(m)

                        val q=TextView(applicationContext)
                        q.text="Esfuerzo =  "+calcParcialKn+" MPa"+"\n"+"                   ("+Math.round(calcParcialKn*10.1972)+"kg/cm² aprox)"
                        q.setTextColor(Color.RED)
                        q.gravity=Gravity.CENTER_HORIZONTAL
                        q.setTextSize(15F)
                        contenedorResultados!!.addView(q)

                        val spaceX=TextView(applicationContext)
                        spaceX.text=""
                        spaceX.height=50
                        contenedorResultados!!.addView(spaceX)
                    }
                    else{
                        val q=TextView(applicationContext)
                        q.text="Esfuerzo s/corrección="+calcParcialKn+" MPa"
                        q.gravity=Gravity.CENTER_HORIZONTAL
                        contenedorResultados!!.addView(q)

                        val h=TextView(applicationContext)
                        h.text="L/D="+ld+"  →  Factor="+facCorrec
                        h.gravity=Gravity.CENTER_HORIZONTAL
                        contenedorResultados!!.addView(h)

                        calcParcialKn=Math.round(calcParcialKn*facCorrec*10)/10.0

                        val i=TextView(applicationContext)
                        i.text="Esfuerzo =  "+calcParcialKn+" MPa"+"\n"+"                   ("+Math.round(calcParcialKn*10.1972)+"kg/cm² aprox)"
                        i.setTextColor(Color.DKGRAY)
                        i.gravity=Gravity.CENTER_HORIZONTAL
                        i.setTextSize(15F)
                        contenedorResultados!!.addView(i)

                        esfParcils.add(calcParcialKn)

                        val spaceX=TextView(applicationContext)
                        spaceX.text=""
                        spaceX.height=50
                        contenedorResultados!!.addView(spaceX)
                    }
                }
                else{
                    esfParcils.add(calcParcialKn)

                    val q=TextView(applicationContext)
                    q.text="Esfuerzo =  "+calcParcialKn+" MPa"+"\n"+"                   ("+Math.round(calcParcialKn*10.1972)+"kg/cm² aprox)"
                    q.gravity=Gravity.CENTER_HORIZONTAL
                    q.setTextColor(Color.DKGRAY)
                    q.setTextSize(15F)
                    contenedorResultados!!.addView(q)

                    val spaceX=TextView(applicationContext)
                    spaceX.text=""
                    spaceX.height=50
                    contenedorResultados!!.addView(spaceX)
                }


            }

        }

        promEsfuer(esfParcils)
    }
    fun obtenerFactorCorreccion(ld:Double,x:Int):Double{
        var facCorre=0.0

        if(ld>1.75){
            Toast.makeText(this, "L/D en Testigo 0"+(x+1)+" es mayor a 1.75",Toast.LENGTH_SHORT).show()
            facCorre=-1.0
        }
        else{
            if(ld<=1.75&&ld>1.5){
                facCorre=extrapolar(ld,1.75,0.98,1.5,0.96)
            }
            else if(ld<=1.5&&ld>1.25){
                facCorre=extrapolar(ld,1.5,0.96,1.25,0.93)
            }
            else if(ld in 1.0..1.25){
                facCorre=extrapolar(ld, 1.25,0.93,1.00,0.87)
            }
            else if(ld<1.0){
                Toast.makeText(this, "El L/D en Testigo 0"+(x+1)+" es:"+ld+", no esta contemplada en la norma",Toast.LENGTH_SHORT).show()
                facCorre=-2.0
            }
        }

        return facCorre
    }
    fun extrapolar(ld:Double, ldMa:Double, ldMaFac:Double, ldMe:Double,ldMeFac:Double):Double{
        var fac:Double=0.0
        fac=Math.round((((ld-ldMe)*(ldMaFac-ldMeFac))/(ldMa-ldMe)+ldMeFac)*1000)/1000.0
        return fac
    }
    fun promEsfuer(num: ArrayList<Double>) {
        var pbv=0.0
        for (x in 0 until num.size){
            pbv+=num[x]
        }

        if(num.size!=1) {
            val titu=TextView(applicationContext)
            titu.text="ESFUERZO PROMEDIO"
            titu.setTextColor(Color.BLACK)
            titu.gravity=Gravity.CENTER_HORIZONTAL
            titu.setTextSize(22F)
            contenedorResultados!!.addView(titu)
            val re = TextView(applicationContext)
            if (!cbKn!!.isChecked) {
                var pe = Math.round(pbv / (num.size) * 1.0)
                re.text =
                    "" + pe + " kg/cm² aprox." + "\n" + Math.round(pe * 0.0980665 * 10) / 10.0 + " MPa aprox."+"\n"
            } else {
                var pe = Math.round(pbv / (num.size) * 10.0) / 10.0
                re.text =
                    "" + pe + " MPa aprox" + "\n" + Math.round(pe * 10.1972) + " kg/cm² aprox."+"\n"
            }
            re.setTextColor(Color.BLACK)
            re.gravity = Gravity.CENTER_HORIZONTAL
            re.setTextSize(18F)
            contenedorResultados!!.addView(re)

            val k=TextView(applicationContext)
            k.setTextSize(15F)
            k.gravity = Gravity.CENTER_HORIZONTAL
            k.text="Debe tomar en cuenta la variación de esfuerzos"+"\n"+"\n"+"\n"
            contenedorResultados!!.addView(k)

            val l=TextView(applicationContext)
            l.setTextSize(17F)
            l.gravity = Gravity.CENTER_HORIZONTAL
            l.text="Rangos aceptables de fuerzas de cilindro individual"
            l.setBackgroundColor(Color.DKGRAY)
            l.setTextColor(Color.WHITE)
            contenedorResultados!!.addView(l)

            val a=TextView(applicationContext)
            a.setTextSize(11F)
            a.gravity = Gravity.CENTER_HORIZONTAL
            a.text="Diámetro | Condiciones | 2cilindros | 3cilindros"

            a.setBackgroundColor(Color.BLACK)
            a.setTextColor(Color.WHITE)
            contenedorResultados!!.addView(a)

            val z=TextView(applicationContext)
            z.setTextSize(11F)
            z.gravity = Gravity.CENTER_HORIZONTAL
            z.text="6x12in       Laboratorio          6.6%          7.8%   "

            z.setBackgroundColor(Color.LTGRAY)
            z.setTextColor(Color.BLACK)
            contenedorResultados!!.addView(z)

            val q=TextView(applicationContext)
            q.setTextSize(11F)
            q.gravity = Gravity.CENTER_HORIZONTAL
            q.text="6x12in           Campo              8.0%          9.5%   "

            q.setBackgroundColor(Color.WHITE)
            q.setTextColor(Color.BLACK)
            contenedorResultados!!.addView(q)

            val p=TextView(applicationContext)
            p.setTextSize(11F)
            p.gravity = Gravity.CENTER_HORIZONTAL
            p.text=" 4x8in             Campo              9.0%        10.5%   "
            p.setBackgroundColor(Color.LTGRAY)
            p.setTextColor(Color.BLACK)
            contenedorResultados!!.addView(p)




        }

    }


}