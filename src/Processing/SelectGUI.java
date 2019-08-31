package Processing;

import processing.core.PVector;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * 该类用于显示图形用户界面
 * @author Jayce
 */
class SelectGUI {
    int starsCount;                 //天体数量
    PVector mass;                   //质量区间
    PVector speed;                  //速度区间

    Body body = null;                   //临时存放单个天体
    private JFrame selectForm;          //选择参数的窗体
    private Font boldFont = new Font("宋体",Font.BOLD,24);            //字体  宋体  粗体样式
    private Font plainFont = new Font("宋体",Font.PLAIN,24);          //字体  宋体  普通样式
    ArrayList<Body> planets = new ArrayList<>();                      //存放行星
    private ArrayList<String[]> planetsInfo = new ArrayList<>();      //在表格中展示的行星信息
    private String[][] initInfo = new String[][] {{"M1","0.0","(0,0)","(0,0)"},{"暂无数据","暂无数据","暂无数据","暂无数据"}};  //初始化信息
    private boolean is_null = true;     //天体数量是否为空

    //文本框
    private JTextField planetsNumber;       //行星数量
    private JTextField planetsMinMass;      //行星最小质量
    private JTextField planetsMaxMass;      //行星最大质量
    private JTextField planetsMinSpeed;     //行星最小速度
    private JTextField planetsMaxSpeed;     //行星最大速度
    private JRadioButton yes_Add;           //是否加入中央恒星
    private JTextField planetsName;         //单个天体名称
    private JTextField planetsMass;         //单个天体质量
    private JTextField planetsPosition;     //单个天体的初始位置
    private JTextField planetsSpeed;        //单个天体的初始速度

    SelectGUI(){}

    /**
     * 显示选择参数的窗体
     */
    void ShowSelect(){
        selectForm = new JFrame("选择参数");    //设置窗体名称
        selectForm.setSize(800,600);  //设置窗体大小
        selectForm.setLocationRelativeTo(null);      //设置窗体位置为屏幕正中央
        selectForm.setLayout(null);                  //设置窗体布局为绝对位置

        //自定义页面
        JPanel customize = new JPanel();
        customize.setLayout(new BorderLayout());
            String[] columnNames = new String[] { "id/name", "质量", "初始位置", "初始速度" };  //表头的名称
            DefaultTableModel tableModel = new DefaultTableModel(initInfo, columnNames);
            JTable table = new JTable(tableModel){
                public boolean isCellEditable(int row,int column){
                    return false;
                }
            };
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //单选
            table.setRowHeight(50);          //设置每一行高度
            table.setFont(plainFont);        //设置内容字体
            // 设置table内容居中
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
            tcr.setHorizontalAlignment(JLabel.CENTER);
            table.setDefaultRenderer(Object.class,tcr);
            //设置表头字体
            table.getTableHeader().setFont(new Font("楷体",Font.BOLD,24));
            JScrollPane sp = new JScrollPane(table);
            sp.setViewportView(table);

            JPanel control = new JPanel(new FlowLayout(FlowLayout.CENTER,20,20));
            JButton confirm = new JButton("确认");
            confirm.setFont(boldFont);
            confirm.addActionListener(e -> {
                CelestialMotionSimulation.method = 1;
//                int option = JOptionPane.showConfirmDialog(selectForm, "镜头是否跟随最大天体 ？(是:跟随  否:镜头保持不动)");
//                switch (option) {
//                    case JOptionPane.YES_OPTION:
//                        CelestialMotionSimulation.conversionLens = true;
//                        break;
//                    case JOptionPane.NO_OPTION:
//                        CelestialMotionSimulation.conversionLens = false;
//                        break;
//                }
                GetParam();
            });
            JButton addStar = new JButton("增加");
            addStar.setFont(boldFont);
            addStar.addActionListener(e -> {
                ParamModel(selectForm);
                if(body != null) {
                    String[] addRow = new String[4];
                    addRow[0] = body.id;
                    addRow[1] = ""+body.mass;
                    addRow[2] = "("+body.pos.x+","+body.pos.y+")";
                    addRow[3] = "("+body.vel.x+","+body.vel.y+")";
                    if(is_null){
                        tableModel.getDataVector().clear();   //清空表数据
                        is_null = false;
                    }else {
                        for(String[] str:planetsInfo){
                            if(body.id.equals(str[0])) {
                                JOptionPane.showMessageDialog(selectForm, "id不能重复");
                                return;
                            }
                        }
                    }
                    planetsInfo.add(addRow);
                    tableModel.addRow(addRow);
                    planets.add(body);
                }
            });
            JButton delStar = new JButton("删除");
            delStar.setFont(boldFont);
            delStar.addActionListener(e -> {
                int option = JOptionPane.showConfirmDialog(selectForm, "是否删除此行 ？");
                if(JOptionPane.YES_OPTION == option){
                    int selectedRow = table.getSelectedRow();
                    if(selectedRow != -1){
                        tableModel.removeRow(selectedRow);
                        if(!is_null) {
                            planetsInfo.remove(selectedRow);
                            planets.remove(selectedRow);
                        }
                    }else {
                        JOptionPane.showMessageDialog(selectForm, "您尚未选中某一行");
                    }
                }
            });
        control.add(confirm);
        control.add(addStar);
        control.add(delStar);
        customize.add(control,BorderLayout.SOUTH);
        customize.add(sp);

        //随机页面
        JPanel myRandom = new JPanel(new GridLayout(8,1));
        ArrayList<JPanel> Lines = new ArrayList<>();
        for(int i = 0; i < 8; i++){
            JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,20,20));
            Lines.add(jPanel);
        }
            JLabel planetsNumberText = new JLabel("请输入行星数量：    ");
            planetsNumberText.setFont(boldFont);
            planetsNumber = new JTextField("200",15);
            planetsNumber.setFont(boldFont);
            Lines.get(0).add(planetsNumberText);
            Lines.get(0).add(planetsNumber);

            JLabel planetsMinMassText = new JLabel("请输入行星最小质量：");
            planetsMinMassText.setFont(boldFont);
            planetsMinMass = new JTextField("100.0",15);
            planetsMinMass.setFont(boldFont);
            Lines.get(1).add(planetsMinMassText);
            Lines.get(1).add(planetsMinMass);

            JLabel planetsMaxMassText = new JLabel("请输入行星最大质量：");
            planetsMaxMassText.setFont(boldFont);
            planetsMaxMass = new JTextField("1000.0",15);
            planetsMaxMass.setFont(boldFont);
            Lines.get(2).add(planetsMaxMassText);
            Lines.get(2).add(planetsMaxMass);

            JLabel planetsMinSpeedText = new JLabel("请输入行星最小速度：");
            planetsMinSpeedText.setFont(boldFont);
            planetsMinSpeed = new JTextField("-5.0",15);
            planetsMinSpeed.setFont(boldFont);
            Lines.get(3).add(planetsMinSpeedText);
            Lines.get(3).add(planetsMinSpeed);

            JLabel planetsMaxSpeedText = new JLabel("请输入行星最大速度：");
            planetsMaxSpeedText.setFont(boldFont);
            planetsMaxSpeed = new JTextField("5.0",15);
            planetsMaxSpeed.setFont(boldFont);
            Lines.get(4).add(planetsMaxSpeedText);
            Lines.get(4).add(planetsMaxSpeed);

            JLabel is_AddMidStar = new JLabel("是否加入中央恒星:");
            is_AddMidStar.setFont(boldFont);
            Lines.get(5).add(is_AddMidStar);
            yes_Add = new JRadioButton("是");
            yes_Add.setFont(boldFont);
            JRadioButton no_Add = new JRadioButton("否");
            no_Add.setFont(boldFont);
            no_Add.setSelected(true);
            ButtonGroup bg = new ButtonGroup();
            bg.add(yes_Add);
            bg.add(no_Add);
            Lines.get(5).add(yes_Add);
            Lines.get(5).add(no_Add);
            yes_Add.addActionListener(e -> {
                ParamModel(selectForm);
                //System.out.println("test");
            });

            JButton ok = new JButton("确认");
            ok.setFont(boldFont);
            ok.addActionListener(e -> {
                CelestialMotionSimulation.method = 2;
//                int option = JOptionPane.showConfirmDialog(selectForm, "镜头是否跟随最大天体 ？(是:跟随  否:镜头保持不动)");
//                switch (option) {
//                    case JOptionPane.YES_OPTION:
//                        CelestialMotionSimulation.conversionLens = true;
//                        break;
//                    case JOptionPane.NO_OPTION:
//                        CelestialMotionSimulation.conversionLens = false;
//                        break;
//                }
                GetParam();
            });
            Lines.get(7).setLayout(new FlowLayout(FlowLayout.CENTER));
            Lines.get(7).add(ok);
            for(JPanel l : Lines){
                myRandom.add(l);
            }

        JTabbedPane tp = new JTabbedPane();
        tp.add(customize);
        tp.add(myRandom);
        tp.setTitleAt(0,"自定义参数");
        tp.setTitleAt(1,"随机参数");
        tp.setFont(boldFont);
        tp.setSelectedIndex(1);

        selectForm.setContentPane(tp);
        selectForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        selectForm.setResizable(false);
        selectForm.setVisible(true);
        selectForm.getRootPane().setDefaultButton(ok);
    }

    /**
     * 行星参数模板
     * @param F 父类窗体
     */
    private void ParamModel(JFrame F){
        JDialog starParam = new JDialog(F,"单个天体的参数",true);
        starParam.setSize(500,550);
        starParam.setLocationRelativeTo(null);
        starParam.setLayout(null);

        JPanel P = new JPanel(new GridLayout(6,1,10,10));
        ArrayList<JPanel> Lines = new ArrayList<>();
        for(int i = 0; i < 6; i++){
            JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,40));
            Lines.add(jPanel);
        }
            JLabel planetsNameText = new JLabel("请输入天体名称：    ");
            planetsNameText.setFont(boldFont);
            planetsName = new JTextField("POSS",15);
            planetsName.setFont(boldFont);
            Lines.get(0).add(planetsNameText);
            Lines.get(0).add(planetsName);

            JLabel planetsMassText = new JLabel("请输入天体质量：    ");
            planetsMassText.setFont(boldFont);
            planetsMass = new JTextField("10000.0",15);
            planetsMass.setFont(boldFont);
            Lines.get(1).add(planetsMassText);
            Lines.get(1).add(planetsMass);

            JLabel planetsPositionText = new JLabel("请输入天体初始位置：");
            planetsPositionText.setFont(boldFont);
            planetsPosition = new JTextField("800.0,450.0",15);
            planetsPosition.setFont(boldFont);
            Lines.get(2).add(planetsPositionText);
            Lines.get(2).add(planetsPosition);

            JLabel planetsSpeedText = new JLabel("请输入天体初始速度：");
            planetsSpeedText.setFont(boldFont);
            planetsSpeed = new JTextField("0.0,0.0",15);
            planetsSpeed.setFont(boldFont);
            Lines.get(3).add(planetsSpeedText);
            Lines.get(3).add(planetsSpeed);

            JButton ok = new JButton("确认");
            ok.setFont(boldFont);
            ok.addActionListener(e -> {
                if(planetsName.getText().trim().equals("")
                        || planetsMass.getText().trim().equals("")
                        || planetsPosition.getText().trim().equals("")
                        || planetsSpeed.getText().trim().equals("")){
                    JOptionPane.showMessageDialog(starParam, "内容不能为空");
                }else {
                    body = UpdateStar(starParam);
                    if(body != null)
                        starParam.setVisible(false);
                }
            });
            Lines.get(5).setLayout(new FlowLayout(FlowLayout.CENTER));
            Lines.get(5).add(ok);

        for(JPanel l : Lines){
            P.add(l);
        }

        starParam.setContentPane(P);
        starParam.setResizable(false);
        starParam.setVisible(true);
    }

    /**
     * 获取文本信息  转化为参数
     */
    private void GetParam(){
        if (CelestialMotionSimulation.method == 2) {
            float Mx, My, Vx, Vy;
            if (planetsNumber.getText().trim().equals("")
                    || planetsMinMass.getText().trim().equals("")
                    || planetsMaxMass.getText().trim().equals("")
                    || planetsMinSpeed.getText().trim().equals("")
                    || planetsMaxSpeed.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(selectForm, "内容不能为空");
                return;
            } else {
                try {
                    this.starsCount = Integer.parseInt(planetsNumber.getText().trim());
                    Mx = Float.parseFloat(planetsMinMass.getText().trim());
                    My = Float.parseFloat(planetsMaxMass.getText().trim());
                    Vx = Float.parseFloat(planetsMinSpeed.getText().trim());
                    Vy = Float.parseFloat(planetsMaxSpeed.getText().trim());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(selectForm, "只能包含数字");
                    return;
                }
                mass = new PVector(Mx, My);
                speed = new PVector(Vx, Vy);
            }
            if (!yes_Add.isSelected()) {
                body = null;
            }
        }
        selectForm.setVisible(false);
        CelestialMotionSimulation.Run();
    }

    /**
     * 把填写的单个天体信息加入到表格中,同时判断格式是否正确
     * @param jDialog  在父类窗体下显示信息框
     * @return body    从文本框中获取的信息赋值给body对象
     */
    private Body UpdateStar(JDialog jDialog){
        if(planetsName.getText().trim().equals("")
                || planetsMass.getText().trim().equals("")
                || planetsPosition.getText().trim().equals("")
                || planetsSpeed.getText().trim().equals("")){
            JOptionPane.showMessageDialog(jDialog, "内容不能为空");
            return null;
        }

        Body bodyTemp = new Body();
        bodyTemp.setId(planetsName.getText().trim());

        float M,Vx,Vy,Px,Py;
        try{
            String[] xy;
            xy = SplitTextField(planetsPosition);
            Px = Float.parseFloat(xy[0]);
            Py = Float.parseFloat(xy[1]);

            xy = SplitTextField(planetsSpeed);
            Vx = Float.parseFloat(xy[0]);
            Vy = Float.parseFloat(xy[1]);
            M = Float.parseFloat(planetsMass.getText());
        }catch (NumberFormatException e){
            //e.printStackTrace();
            JOptionPane.showMessageDialog(jDialog, "格式错误");
            return null;
        }
        bodyTemp.setPos(new PVector(Px,Py));
        bodyTemp.setVel(new PVector(Vx,Vy));
        bodyTemp.setMass(M);
        return bodyTemp;
    }

    /**
     * 分隔文本框字符串
     * 分隔符:空格 英文逗号 中文逗号
     * @param textField 分隔该文本框的字符
     * @return splitStr 返回分隔后的字符串数组
     */
    private String[] SplitTextField(JTextField textField){
        String[] splitStr;
        if(textField.getText().contains(","))
            splitStr = textField.getText().trim().split(",");
        else if(textField.getText().contains("，"))
            splitStr = textField.getText().trim().split("，");
        else
            splitStr = textField.getText().trim().split(" ");
        return splitStr;
    }
}
