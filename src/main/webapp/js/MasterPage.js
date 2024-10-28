// ckeditor
    window.onload = function() {
                    CKEDITOR.ClassicEditor.create(document.getElementById('content'), option);
                }
                function FormCheck() {
                    // 입력 필드의 값을 가져오고 앞뒤 공백을 제거
                        var title = document.getElementById('title').value.trim();

                         // 제목이 비어있는지 검사
                        if (title === ' ') {
                            alert('글 제목을 입력하세요.');
                            return false;
                        }

                        // 제목에 큰따옴표(")나 작은따옴표(')가 있는지 검사
                        if (title.indexOf('"') !== -1 || title.indexOf("'") !== -1) {
                            alert('제목에 큰따옴표(")나 작은따옴표(\')를 사용할 수 없습니다.');
                            return false;
                        }
                    return true;
                }



// ------------------------------------------------------
  // 검색 기능
        function searchTable() {
            var input, filter, table, tr, td, i, j, txtValue;
            input = document.getElementById("searchInput");
            filter = input.value.toLowerCase();
            table = document.querySelector(".user-list tbody");
            tr = table.getElementsByTagName("tr");

            for (i = 0; i < tr.length; i++) {
                tr[i].style.display = "none"; // 기본적으로 숨김 처리
                td = tr[i].getElementsByTagName("td");
                for (j = 0; j < td.length; j++) {
                    if (td[j]) {
                        txtValue = td[j].textContent || td[j].innerText;
                        if (txtValue.toLowerCase().indexOf(filter) > -1) {
                            tr[i].style.display = ""; // 검색어와 일치하면 보여줌
                            break;
                        }
                    }
                }
            }
        }

        // 테이블 정렬 기능
        function sortTable(columnIndex) {
            var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
            table = document.querySelector(".user-list");
            switching = true;
            dir = "asc"; // 기본 정렬 방향: 오름차순
            var headers = table.querySelectorAll("th");
                headers.forEach(function(header) {
                    header.innerHTML = header.innerHTML.replace(" ▼", "").replace(" ▲", "");
                });

                // 현재 정렬할 컬럼에 화살표 추가
                headers[columnIndex].innerHTML += dir === "asc" ? " ▼" : " ▲";

                while (switching) {
                    switching = false;
                    rows = table.rows;
                    for (i = 1; i < (rows.length - 1); i++) {
                        shouldSwitch = false;
                        x = rows[i].getElementsByTagName("TD")[columnIndex];
                        y = rows[i + 1].getElementsByTagName("TD")[columnIndex];
                        if (dir == "asc") {
                            if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                                shouldSwitch = true;
                                break;
                            }
                        } else if (dir == "desc") {
                            if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
                                shouldSwitch = true;
                                break;
                            }
                        }
                    }
                    if (shouldSwitch) {
                        rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
                        switching = true;
                        switchcount++;
                    } else {
                        if (switchcount == 0 && dir == "asc") {
                            dir = "desc";
                            headers[columnIndex].innerHTML = headers[columnIndex].innerHTML.replace(" ▼", " ▲");
                            switching = true;
                        }
                    }
            }
        }

        function searchTable1() {
                    var input, filter, table, tr, td, i, j, txtValue;
                    input = document.getElementById("searchInput");
                    filter = input.value.toLowerCase();
                    table = document.querySelector(".anime-list tbody");
                    tr = table.getElementsByTagName("tr");

                    for (i = 0; i < tr.length; i++) {
                        tr[i].style.display = "none"; // 기본적으로 숨김 처리
                        td = tr[i].getElementsByTagName("td");
                        for (j = 0; j < td.length; j++) {
                            if (td[j]) {
                                txtValue = td[j].textContent || td[j].innerText;
                                if (txtValue.toLowerCase().indexOf(filter) > -1) {
                                    tr[i].style.display = ""; // 검색어와 일치하면 보여줌
                                    break;
                                }
                            }
                        }
                    }
                }

                // 테이블 정렬 기능
                function sortTable1(columnIndex) {
                    var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
                    table = document.querySelector(".anime-list");
                    switching = true;
                    dir = "asc"; // 기본 정렬 방향: 오름차순
                    var headers = table.querySelectorAll("th");
                      headers.forEach(function(header) {
                                                        header.innerHTML = header.innerHTML.replace(" ▼", "").replace(" ▲", "");
                                                    });

                                                           // 현재 정렬할 컬럼에 화살표 추가
                                                                                    headers[columnIndex].innerHTML += dir === "asc" ? " ▼" : " ▲";
                    while (switching) {
                        switching = false;
                        rows = table.rows;
                        for (i = 1; i < (rows.length - 1); i++) {
                            shouldSwitch = false;
                            x = rows[i].getElementsByTagName("TD")[columnIndex];
                            y = rows[i + 1].getElementsByTagName("TD")[columnIndex];
                            if (dir == "asc") {
                                if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                                    shouldSwitch = true;
                                    break;
                                }
                            } else if (dir == "desc") {
                                if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
                                    shouldSwitch = true;
                                    break;
                                }
                            }
                        }
                        if (shouldSwitch) {
                            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
                            switching = true;
                            switchcount++;
                        } else {
                            if (switchcount == 0 && dir == "asc") {
                                dir = "desc";
                                headers[columnIndex].innerHTML = headers[columnIndex].innerHTML.replace(" ▼", " ▲");
                                switching = true;
                            }
                        }
                    }
                }

                 function searchTable2() {
                            var input, filter, table, tr, td, i, j, txtValue;
                            input = document.getElementById("searchInput");
                            filter = input.value.toLowerCase();
                            table = document.querySelector(".store-list tbody");
                            tr = table.getElementsByTagName("tr");

                            for (i = 0; i < tr.length; i++) {
                                tr[i].style.display = "none"; // 기본적으로 숨김 처리
                                td = tr[i].getElementsByTagName("td");
                                for (j = 0; j < td.length; j++) {
                                    if (td[j]) {
                                        txtValue = td[j].textContent || td[j].innerText;
                                        if (txtValue.toLowerCase().indexOf(filter) > -1) {
                                            tr[i].style.display = ""; // 검색어와 일치하면 보여줌
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        // 테이블 정렬 기능
                        function sortTable2(columnIndex) {
                            var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
                            table = document.querySelector(".store-list");
                            switching = true;
                            dir = "asc"; // 기본 정렬 방향: 오름차순
                            var headers = table.querySelectorAll("th");
                                headers.forEach(function(header) {
                                    header.innerHTML = header.innerHTML.replace(" ▼", "").replace(" ▲", "");
                                });

                                // 현재 정렬할 컬럼에 화살표 추가
                                headers[columnIndex].innerHTML += dir === "asc" ? " ▼" : " ▲";

                                while (switching) {
                                    switching = false;
                                    rows = table.rows;
                                    for (i = 1; i < (rows.length - 1); i++) {
                                        shouldSwitch = false;
                                        x = rows[i].getElementsByTagName("TD")[columnIndex];
                                        y = rows[i + 1].getElementsByTagName("TD")[columnIndex];
                                        if (dir == "asc") {
                                            if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                                                shouldSwitch = true;
                                                break;
                                            }
                                        } else if (dir == "desc") {
                                            if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
                                                shouldSwitch = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (shouldSwitch) {
                                        rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
                                        switching = true;
                                        switchcount++;
                                    } else {
                                        if (switchcount == 0 && dir == "asc") {
                                            dir = "desc";
                                            headers[columnIndex].innerHTML = headers[columnIndex].innerHTML.replace(" ▼", " ▲");
                                            switching = true;
                                        }
                                    }
                            }
                        }

// ------------------------------------------------------
// 일/월별 매출 관리

 $(document).ready(function() {
     $.ajax({
         type: 'GET',
         url: '/master/getCombinedSalesData', // 요청 URL 확인
         success: function(response) {
             const aniSalesData = response.aniSalesData;
             const dailySalesData = response.dailySalesData;

             const labels = dailySalesData.map(data => data.orderDate);
             const totalSales = dailySalesData.map(data => data.totalSales);
             const aniSales = aniSalesData.map(data => data.totalSales || 0); // 기본값 0 설정

             // 차트 데이터 설정
             const chartData = {
                 labels: labels,
                 datasets: [
                     {
                         label: '일별 매출',
                         data: totalSales,
                         borderColor: 'rgba(54, 162, 235, 1)',
                         backgroundColor: 'rgba(54, 162, 235, 0.2)',
                         fill: true,
                         yAxisID: 'y-axis-sales',  // 첫 번째 Y축과 연결
                     },
                     {
                         label: '상품 ani_title 매출',
                         data: aniSales,
                         borderColor: 'rgba(255, 159, 64, 1)',
                         backgroundColor: 'rgba(255, 159, 64, 0.2)',
                         fill: true,
                         yAxisID: 'y-axis-aniSales', // 두 번째 Y축과 연결
                     }
                 ]
             };

             const ctx = document.getElementById('combinedDailyChartView').getContext('2d');
             const combinedChart = new Chart(ctx, {
                 type: 'line',
                 data: chartData,
                 options: {
                     responsive: true,
                     scales: {
                         'y-axis-sales': { // 첫 번째 Y축 (일별 매출)
                             type: 'linear',
                             position: 'left',
                             beginAtZero: true,
                             title: {
                                 display: true,
                                 text: '일별 매출'
                             },
                             ticks: {
                                 callback: function(value) {
                                     return value + ' 원'; // y축 단위를 '원'으로 표시
                                 }
                             }
                         },
                         'y-axis-aniSales': { // 두 번째 Y축 (애니메이션 매출)
                             type: 'linear',
                             position: 'right',
                             beginAtZero: true,
                             title: {
                                 display: true,
                                 text: '상품 ani_title 매출'
                             },
                             grid: {
                                 drawOnChartArea: false // 두 번째 Y축의 그리드 라인 제거
                             },
                             ticks: {
                                 callback: function(value) {
                                     return value + ' 원'; // y축 단위를 '원'으로 표시
                                 }
                             }
                         },
                         x: {  // X축 설정
                             title: {
                                 display: true,
                                 text: '날짜'
                             }
                         }
                     },
                     plugins: {
                         legend: {
                             display: true,
                             position: 'top'
                         }
                     }
                 }
             });
         },
         error: function() {
             alert('데이터를 가져오는 데 실패했습니다.');
         }
     });
 });





   document.addEventListener('DOMContentLoaded', function () {
       // 백엔드 API에서 데이터 가져오기
       fetch('/master/registrationChart')
           .then(response => response.json())
           .then(data => {
               // 날짜와 가입 수 데이터를 추출
               const dates = data.map(item => item.date);
               const counts = data.map(item => item.count);

               // 차트 생성
               const ctx = document.getElementById('registrationChart').getContext('2d');
               const registrationChart = new Chart(ctx, {
                   type: 'line',
                   data: {
                       labels: dates,
                       datasets: [{
                           label: '회원 가입 수',
                           data: counts,
                           fill: false,
                           borderColor: 'black',
                           tension: 0.4
                       }]
                   },
                  options: {
                      responsive: true,
                      scales: {
                          x: {
                              title: {
                                  display: true,
                                  text: '날짜'
                              },
                              grid: {
                                  display: false // X축 격자선 숨기기
                              }
                          },
                          y: {
                              title: {
                                  display: true,
                                  text: '가입자 수'
                              },
                              grid: {
                                  display: false // Y축 격자선 숨기기
                              },
                              beginAtZero: true
                          }
                      },
                      plugins: {
                          legend: {
                              display: true,
                              position: 'top'
                          }
                      }
                  }
               });
           })
           .catch(error => {
               console.error('회원 가입 데이터를 가져오는 중 오류가 발생했습니다:', error);
           });
   });

    function filterAnime() {
        const selectedCategory = document.getElementById('filterSelect').value;
        const rows = document.querySelectorAll('#animeTableBody tr');

        rows.forEach(row => {
            const categoryCode = row.getAttribute('data-anitype');
            if (selectedCategory === '' || categoryCode === selectedCategory) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    }

    function filterStore() {
            const selectedCategory = document.getElementById('filterSelect').value;
            const rows = document.querySelectorAll('#storeTableBody tr');

            rows.forEach(row => {
                const categoryCode = row.getAttribute('data-category');
                if (selectedCategory === '' || categoryCode === selectedCategory) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        }
