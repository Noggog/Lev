/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lev.gui;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import lev.Ln;

/**
 *
 * @author Justin Swanson
 */
public class LTree extends JTree {

    /**
     *
     */
    protected DefaultTreeModel model;
    String state;

    /**
     *
     * @param width
     * @param height
     */
    public LTree(int width, int height) {
        this();
        setSize(width, height);
    }

    public LTree() {
	super();
	model = (DefaultTreeModel) this.getModel();

	setRowHeight(20);
        setVisible(true);
    }
    
    public void clearTree() {
	setModel(new DefaultTreeModel(new DefaultMutableTreeNode()));
    }

    public void setRoot(TreeNode n) {
	setModel(new DefaultTreeModel(n));
    }

    public void expandToDepth(int depth) {
	Ln.expandToDepth(this, depth);
    }

    /**
     *
     * @param b
     */
    public void expand(Boolean b) {
        Ln.expandAll(this, b);
    }

    /**
     *
     */
    public void expandRoot() {
        expandPath(new TreePath((TreeNode) getModel().getRoot()));
    }

    /**
     *
     * @return
     */
    public int getTotalRowHeight() {
        return getRowCount() * getRowHeight();
    }
    
    /**
     *
     * @return
     */
    public ArrayList<Integer> getExpandedRows() {
        ArrayList<Integer> out = new ArrayList<>();
        TreePath path;
        for (int i = 0; i < getRowCount(); i++) {
            path = getPathForRow(i);
            if (isExpanded(path)) {
                out.add(i);
            }
        }
        return out;
    }

    /**
     *
     * @param rows
     */
    public void expandRows(ArrayList<Integer> rows) {
        for (int i : rows) {
            TreePath path = getPathForRow(i);
            expandPath(path);
        }
    }

    /**
     *
     * @return
     */
    public TreeNode getRoot() {
        return (TreeNode) getModel().getRoot();
    }

    /**
     *
     * @return
     */
    public ArrayList<Integer> rootRows() {
        ArrayList<Integer> out = new ArrayList<>();

        TreePath rootPath = new TreePath(getRoot());
        TreeNode root = (TreeNode) rootPath.getLastPathComponent();
        if (root.getChildCount() >= 0) {
            for (Enumeration e = root.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = rootPath.pathByAddingChild(n);
                out.add(getRowForPath(path));
            }
        }
        return out;
    }

    /**
     *
     * @param row
     * @return
     */
    public String getExpansionState(int row) {
	return Ln.getExpansionState(this, row);
    }

    /**
     *
     * @param row
     * @param state
     */
    public void restoreExpansionState (int row, String state) {
	Ln.restoreExpanstionState(this, row, state);
    }

    /**
     *
     */
    public void saveExpansionState () {
	state = getExpansionState(0);
    }

    /**
     *
     */
    public void restoreExpansionState () {
	restoreExpansionState(0, state);
    }

    /**
     *
     * @param node
     */
    public void nodeChanged (TreeNode node) {
	model.nodeChanged(node);
    }
}
