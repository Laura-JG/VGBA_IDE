import javax.swing.JTree;
import javax.swing.tree.TreePath;

public class TreeState {

private final JTree tree;
private StringBuilder sb;

public TreeState(JTree tree){
    this.tree = tree;
}

public String getExpansionState(){

    sb = new StringBuilder();

    for(int i =0 ; i < tree.getRowCount(); i++){
        TreePath tp = tree.getPathForRow(i);
        if(tree.isExpanded(i)){
            sb.append(tp.toString());
            sb.append(",");
        }
    }

    return sb.toString();

}   

public void setExpansionState(String s){

    for(int i = 0 ; i<tree.getRowCount(); i++){
        TreePath tp = tree.getPathForRow(i);
        if(s.contains(tp.toString() )){
            tree.expandRow(i);
        }   
    }
}

}