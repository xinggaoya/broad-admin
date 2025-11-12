package com.broad.common.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class TreeUtilsTest {

    static class Node {
        Integer id;
        Integer pid;
        List<Node> children;
        Node(Integer id, Integer pid) { this.id = id; this.pid = pid; }
        Integer getId() { return id; }
        Integer getPid() { return pid; }
        List<Node> getChildren() { return children; }
        void setChildren(List<Node> c) { this.children = c; }
    }

    @Test
    void build_shouldConstructTreeInLinearTime() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(1, 0));
        nodes.add(new Node(2, 1));
        nodes.add(new Node(3, 1));
        nodes.add(new Node(4, 2));

        List<Node> roots = TreeUtils.build(nodes, Node::getId, Node::getPid, Node::setChildren, 0);
        Assertions.assertEquals(1, roots.size());
        Node root = roots.get(0);
        Assertions.assertEquals(1, root.getId());
        Assertions.assertEquals(2, root.getChildren().size());
        Assertions.assertEquals(2, root.getChildren().get(0).getId());
        Assertions.assertEquals(4, root.getChildren().get(0).getChildren().get(0).getId());
        Assertions.assertEquals(3, root.getChildren().get(1).getId());
    }
}

