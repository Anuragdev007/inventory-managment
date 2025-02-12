
    $(document).ready(function () {
      const bulkDeleteContainer = $("#bulk-delete-container");
      const bulkDeleteBtn = $("#bulk-delete-btn");

      // Toggle bulk delete button visibility
      const toggleBulkDeleteButton = () => {
        const anySelected = $(".row-checkbox:checked").length > 0;
        bulkDeleteContainer.toggle(anySelected);
      };

      // Handle "Select All" checkbox
      $("#select-all").on("change", function () {
        const isChecked = $(this).prop("checked");
        $(".row-checkbox").prop("checked", isChecked);
        $(".delete-btn").toggle(isChecked); // Show/hide delete buttons
        toggleBulkDeleteButton();
      });

      // Handle individual checkboxes
      $(".row-checkbox").on("change", function () {
        const row = $(this).closest("tr");
        const isChecked = $(this).prop("checked");
        row.find(".delete-btn").toggle(isChecked); // Show/hide row delete button

        const allChecked =
          $(".row-checkbox").length === $(".row-checkbox:checked").length;
        $("#select-all").prop("checked", allChecked); // Update "Select All" checkbox state
        toggleBulkDeleteButton();
      });

      // Handle row click to toggle checkbox
      $("#productTable tbody").on("click", "tr", function (e) {
        // Prevent selection if clicking on specific inputs or buttons
        if (
          $(e.target).is(
            'input[id^="quantity-"], input[id^="threshold-"], select[id^="vendor-"], button[id^="update"], .row-checkbox'
          )
        ) {
          return; // Exit if the target is any of the excluded elements
        }

        let checkbox = $(this).find(".row-checkbox");
        checkbox.prop("checked", !checkbox.prop("checked")); // Toggle the checkbox
        toggleBulkDeleteButton(); // Update delete button visibility
      });

      // Handle bulk delete
      bulkDeleteBtn.on("click", function () {
        const selectedIds = $(".row-checkbox:checked")
          .map(function () {
            return $(this).val();
          })
          .get();

        if (selectedIds.length > 0) {
          if (
            confirm("Are you sure you want to delete the selected products?")
          ) {
            $.ajax({
              url: "/inventory/bulk-delete",
              type: "POST",
              contentType: "application/json",
              data: JSON.stringify(selectedIds),
              success: function () {
                location.reload();
              },
              error: function (xhr) {
                alert("Error deleting products: " + xhr.responseText);
              },
            });
          }
        }
      });

      // Handle individual delete button
      $("#productTable").on("click", ".delete-btn", function (e) {
        e.stopPropagation(); // Prevent triggering the row click event
        const productId = $(this).data("id");

        if (confirm("Are you sure you want to delete this product?")) {
          $.ajax({
            url: "/inventory/delete/" + productId,
            type: "DELETE",
            success: function () {
              location.reload();
            },
            error: function (xhr) {
              alert("Error deleting product: " + xhr.responseText);
            },
          });
        }
      });
    });

    function showSpinner() {
      $("#loading-spinner").show();
    }

    function hideSpinner() {
      $("#loading-spinner").hide();
    }

    $(document).ready(function () {
      // Handle search input
      $("#search-input").on("input", function () {
        let query = $(this).val().trim();

        if (query.length > 0) {
          $.ajax({
            url: "/search/suggest",
            method: "GET",
            data: { query: query },
            success: function (data) {
              $("#suggestions").empty();
              if (data && data.length > 0) {
                data.forEach(function (product) {
                  $("#suggestions").append(
                    `<div class="suggestion-item" data-id="${product.id}">
                      ${product.name} (${product.sku})
                    </div>`
                  );
                });
                $("#suggestions").show();
              } else {
                $("#suggestions").hide();
              }
            },
            error: function () {
              $("#suggestions").hide();
            },
          });
        } else {
          $("#suggestions").hide();
        }
      });

      // Handle click on suggestion item
      $(document).on("click", ".suggestion-item", function () {
        let productId = $(this).data("id");
        filterInventoryTable(productId);
        $("#suggestions").hide();
        $("#search-input").val($(this).text());
      });

      // Hide suggestions when clicking outside
      $(document).click(function (event) {
        if (!$(event.target).closest("#search-input, #suggestions").length) {
          $("#suggestions").hide();
        }
      });

      // Filter inventory table
      function filterInventoryTable(productId) {
        $("#productTable tbody tr").each(function () {
          let row = $(this);
          row.toggle(row.data("id") === productId);
        });
      }
    });

      // Handle vendor delete button
      $("#vendorTable").on("click", ".delete-vendor-btn", function () {
        let vendorId = $(this).data("id");
        $.ajax({
          url: "/vendor/delete/" + vendorId,
          type: "DELETE",
          success: function () {
            alert("Vendor deleted successfully");
            location.reload();
          },
          error: function (xhr) {
            alert("Error deleting vendor: " + xhr.responseText);
          },
        });
      });

      // Handle product update button
      $("#productTable").on("click", ".update-btn", function () {
        showSpinner();
        let productId = $(this).data("id");
        let productRow = $(this).closest("tr");
        let updatedData = {
          id: productId,
          quantity: productRow.find('input[id^="quantity-"]').val(),
          threshold: productRow.find('input[id^="threshold-"]').val(),
          vendorId: productRow.find('select[id^="vendor-"]').val(),
        };

        // Make AJAX call to update the product
        $.ajax({
          url: "/inventory/update/" + productId,
          type: "PUT",
          contentType: "application/json",
          data: JSON.stringify(updatedData),
          success: function () {
            location.reload();
          },
          error: function (xhr) {
            alert("Error updating product: " + xhr.responseText);
          },
          complete: function () {
            hideSpinner();
          },
        });
      });

      // Handle product delete button
      $("#productTable").on("click", ".delete-btn", function () {
        let productId = $(this).data("id");
        $.ajax({
          url: "/inventory/delete/" + productId,
          type: "DELETE",
          success: function () {
            alert("Product deleted successfully");
            location.reload();
          },
          error: function (xhr) {
            alert("Error deleting product: " + xhr.responseText);
          },
        });
      });

      // Handle vendor form submission for adding a vendor
      $("#update-vendor-form").on("submit", function (e) {
        e.preventDefault();
        let vendorData = {
          id: $("#update-vendor-id").val(),
          name: $("#updateVendorName").val(),
          email: $("#updateVendorEmail").val(),
          phoneNumber: $("#updateVendorPhone").val(),
        };

        $.ajax({
          url: "/vendor/update" + vendorId,
          type: "PUT",
          contentType: "application/json",
          data: JSON.stringify(vendorData),
          success: function () {
            alert("Vendor updated successfully");
            location.reload();
          },
          error: function (xhr) {
            alert("Error updating vendor: " + xhr.responseText);
          },
        });
      });

      // Handle cancel update for vendor
      $("#cancel-update").on("click", function () {
        $("#update-vendor-modal").hide();
      });

      // Show modal to update vendor when vendor row is clicked (you might want to add a button for this)
      $("#vendorTable").on("click", "tr", function () {
        let vendorId = $(this).data("id");
        let vendorName = $(this).find("td").eq(0).text();
        let vendorEmail = $(this).find("td").eq(1).text();
        let vendorPhone = $(this).find("td").eq(2).text();

        $("#update-vendor-id").val(vendorId);
        $("#updateVendorName").val(vendorName);
        $("#updateVendorEmail").val(vendorEmail);
        $("#updateVendorPhone").val(vendorPhone);
        $("#update-vendor-modal").show();
      });
    });



   // Function to toggle the dropdown visibility
         function toggleDropdown() {
             const dropdown = document.getElementById("filterDropdown");
             dropdown.style.display = dropdown.style.display === "block" ? "none" : "block";
         }


         // Hide dropdown when clicking outside
       $(document).click (function (event) {
             if (!event.target.matches('.filter-button')) {
                 const dropdown = document.getElementById("filterDropdown");
                 if (dropdown.style.display === "block") {
                     dropdown.style.display = "none";
                 }
             }
         });
