package xueli.utils.collection;

import java.util.Arrays;
import java.util.Objects;

// Who put it here? —— XueLi
public class SegmentTree {

	private TreeNode[] nodes;

	public SegmentTree(int[] list) {
		Objects.requireNonNull(list);
		this.nodes = new TreeNode[list.length << 2];//Only 4 times of space required
		this.buildTree(1, 1, list.length, list);
		System.out.println(Arrays.toString(this.nodes));

	}

	private void buildTree(int nodeIndex, int left, int right, int[] list) {
		TreeNode node = this.nodes[nodeIndex] = new TreeNode();
		node.left = left;
		node.right = right;
		if (left == right) {
			node.value = list[left - 1];
			return;
		}
//		int middle = (int) Math.sqrt((left * left + right * right) / 2.0);
		int middle = (left + right) >> 1;
//		int middle = (int) (a - b) / (Math.log(a) - Math.log(b));
//		int middle = (int) Math.sqrt(left * right);
//		int middle = (int) 2 / (1.0 / left + 1.0 / right);
		int leftIndex = nodeIndex << 1, rightIndex = leftIndex | 1;
		this.buildTree(leftIndex, left, middle, list);
		this.buildTree(rightIndex, middle + 1, right, list);
		node.value = this.nodes[leftIndex].value + this.nodes[rightIndex].value;
	}

	public int sum(int left, int right) {
		return this.sum(1, left, right);
	}
	
	private void pushdown(int nodeIndex) {
		TreeNode node = this.nodes[nodeIndex];
		TreeNode leftNode = this.nodes[nodeIndex << 1];
		TreeNode rightNode = this.nodes[(nodeIndex << 1) | 1];
		leftNode.lazy += node.lazy;
		rightNode.lazy += node.lazy;
		leftNode.value += node.lazy * (leftNode.right - leftNode.left + 1);
		rightNode.value += node.lazy * (rightNode.right - rightNode.left + 1);
		node.lazy = 0;
	}

	private int sum(int nodeIndex, int left, int right) {
		TreeNode node = this.nodes[nodeIndex];
		if (left <= node.left && right >= node.right)
			return node.value;
		if ((left <= node.right && left >= node.left) || (right <= node.right && right >= node.left)) {
			if(node.lazy != 0) {
				pushdown(nodeIndex);
			}
			int s = 0;
			int leftIndex = nodeIndex << 1, rightIndex = leftIndex | 1;
			s += sum(leftIndex, left, right);
			s += sum(rightIndex, left, right);
			return s;
		}
		return 0;
	}

	private void add(int nodeIndex, int left, int right, int mod) {
		TreeNode node = this.nodes[nodeIndex];
		if(left <= node.left && right >= node.right) {
			node.lazy += mod;
			node.value += (node.right - node.left + 1) * mod;
		}
		if(node.lazy != 0) {
			pushdown(nodeIndex);
		}
		int leftIndex = nodeIndex << 1, rightIndex = leftIndex | 1;
		int middle = (node.left + node.right) >> 1;
		if(left <= middle) {
			add(leftIndex, left, right ,mod);
		}
		if(right > middle) {
			add(rightIndex, left, right, mod);
		}
		node.value = this.nodes[leftIndex].value + this.nodes[rightIndex].value;
	}
	
	public void add(int left, int right, int mod) {
		add(1, left, right, mod);
	}
	
	private static class TreeNode {
		public int value = 0;
		public int left, right;
		public int lazy = 0;

		@Override
		public String toString() {
			return String.format("{%s, %s, %s, %s}", value, lazy, left, right);
		}

	}

	public static void main(String[] args) {
		var tree = new SegmentTree(new int[] { 1, 5, 2, 5, 3, 1, 3, 4, 6, 6, 3, 2, 4, 5, 6, 5, 2, 3 });
		System.out.println(tree.sum(2, 21));

	}

}
