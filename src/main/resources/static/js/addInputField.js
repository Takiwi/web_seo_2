function addNewField() {
  const inputField = document.querySelector(".inputField");

  // create children node
  const inputNode = document.createElement("input");
  inputNode.type = "text";
  inputNode.name = "artistName";

  // add children node
  inputField.append(inputNode);
}

const field = document.getElementById("addNameField");
field.addEventListener("click", addNewField);
