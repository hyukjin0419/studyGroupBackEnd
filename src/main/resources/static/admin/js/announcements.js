/**
 * 공지사항 관리 모듈
 * 공지사항 목록 조회, 등록, 수정, 삭제 기능을 처리합니다.
 */

// 공지사항 관리 모듈 전역 상태
const announcementModule = {
    // 공지사항 데이터 저장소
    announcements: [],
    
    // 초기화 플래그
    initialized: false,
    
    // 모듈 초기화
    async init() {
        console.log('Initializing Announcements Module');
        
        // 이미 초기화되었다면 스킵
        if (this.initialized) {
            console.log('Announcements module already initialized');
            return;
        }
            
        // 공지사항 관리 UI 초기화
        this.initAnnouncementsUI();
        
        // 공지사항 목록 로드
        await this.loadAnnouncements();
        
        // 이벤트 리스너 설정
        this.setupEventListeners();
        
        // Flatpickr 날짜 선택기 초기화
        this.initFlatpickr();
        
        this.initialized = true;
        console.log('Announcements module initialization complete');
    },
    
    // Flatpickr 날짜 선택기 초기화
    initFlatpickr() {
        // 기본 flatpickr 설정
        const flatpickrConfig = {
            enableTime: true,          // 시간 선택 활성화
            dateFormat: "Y-m-d H:i",   // 날짜 형식
            locale: "ko",              // 한국어 지원
            time_24hr: true,           // 24시간 형식
            allowInput: true,          // 수동 입력 허용
            minDate: "today",          // 현재 날짜부터 선택 가능
            plugins: [
                new confirmDatePlugin({
                    confirmText: "확인",
                    cancelText: "취소",
                    showAlways: true,
                    theme: "material_blue"
                })
            ]
        };
        
        // 클래스 변수로 객체 저장(모달 재생성 시 다시 초기화하기 위해)
        this.publishDatepicker = null;
        this.expiryDatepicker = null;
        
        // 게시일/만료일 입력 필드에 flatpickr 연결
        this.initDatepickers();
    },
    
    // 날짜 선택기 연결 초기화 (모달 열릴 때마다 호출)
    initDatepickers() {
        const publishDateInput = document.getElementById('announcement-publish-date');
        const expiryDateInput = document.getElementById('announcement-expiry-date');
        
        if (publishDateInput) {
            // 기존 인스턴스 제거
            if (this.publishDatepicker) {
                this.publishDatepicker.destroy();
            }
            
            // 게시일 초기화
            this.publishDatepicker = flatpickr(publishDateInput, {
                enableTime: true,
                dateFormat: "Y-m-d H:i",
                locale: "ko",
                time_24hr: true,
                allowInput: true,
                plugins: [
                    new confirmDatePlugin({
                        confirmText: "확인",
                        cancelText: "취소",
                        showAlways: true,
                        theme: "material_blue"
                    })
                ],
                onChange: (selectedDates) => {
                    // 만료일 선택기의 최소 날짜 업데이트
                    if (this.expiryDatepicker && selectedDates.length > 0) {
                        // 게시일 다음날로 최소 날짜 설정
                        const minDate = new Date(selectedDates[0]);
                        minDate.setDate(minDate.getDate() + 1);
                        this.expiryDatepicker.set('minDate', minDate);
                    }
                }
            });
        }
        
        if (expiryDateInput) {
            // 기존 인스턴스 제거
            if (this.expiryDatepicker) {
                this.expiryDatepicker.destroy();
            }
            
            // 만료일 초기화
            this.expiryDatepicker = flatpickr(expiryDateInput, {
                enableTime: true,
                dateFormat: "Y-m-d H:i",
                locale: "ko",
                time_24hr: true,
                allowInput: true,
                plugins: [
                    new confirmDatePlugin({
                        confirmText: "확인",
                        cancelText: "취소",
                        showAlways: true,
                        theme: "material_blue"
                    })
                ]
            });
            
            // 게시일 값이 있는 경우, 최소 날짜 설정
            const publishDate = this.publishDatepicker?.selectedDates[0];
            if (publishDate) {
                const minDate = new Date(publishDate);
                minDate.setDate(minDate.getDate() + 1);
                this.expiryDatepicker.set('minDate', minDate);
            }
        }
    },
    
    // backward compatibility
    async initAnnouncements() {
        console.log('Legacy initAnnouncements called, redirecting to init()');
        return await this.init();
    },
    
    // 공지사항 관리 UI 초기화
    initAnnouncementsUI() {
        console.log('Initializing announcements UI');
        const announcementsContent = document.getElementById('announcements-content');
        if (!announcementsContent) {
            console.error('announcements-content element not found');
            return;
        }
        
        // 공지사항 관리 UI HTML
        announcementsContent.innerHTML = `
            <div class="card mb-4">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="mb-0">공지사항 관리</h5>
                    <button type="button" class="btn btn-primary btn-sm" id="btn-add-announcement">
                        <i class="bi bi-plus-circle me-1"></i> 공지사항 등록
                    </button>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>제목</th>
                                    <th>작성자</th>
                                    <th>게시일</th>
                                    <th>만료일</th>
                                    <th>상태</th>
                                    <th>관리</th>
                                </tr>
                            </thead>
                            <tbody id="announcements-table-body">
                                <tr>
                                    <td colspan="7" class="text-center">로딩 중...</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            
            <!-- 공지사항 모달 -->
            <div class="modal fade" id="announcementModal" tabindex="-1" aria-labelledby="announcementModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="announcementModalLabel">공지사항 등록</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form id="announcement-form">
                                <input type="hidden" id="announcement-id">
                                <div class="mb-3">
                                    <label for="announcement-title" class="form-label">제목 *</label>
                                    <input type="text" class="form-control" id="announcement-title" required>
                                </div>
                                <div class="mb-3">
                                    <label for="announcement-content" class="form-label">내용 *</label>
                                    <textarea class="form-control" id="announcement-content" rows="10" required></textarea>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <div class="form-check mt-2">
                                            <input class="form-check-input" type="checkbox" value="" id="announcement-important">
                                            <label class="form-check-label" for="announcement-important">
                                                중요 공지사항
                                            </label>
                                            <div class="form-text">중요 공지사항은 강조 표시됩니다.</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label for="announcement-publish-date" class="form-label">게시일 *</label>
                                        <input type="datetime-local" class="form-control" id="announcement-publish-date" required>
                                        <div class="form-text">
                                            지정한 날짜부터 공지사항이 표시됩니다.<br>
                                            미래 날짜로 설정하면 임시저장 상태가 됩니다.
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="announcement-expiry-date" class="form-label">만료일</label>
                                        <input type="datetime-local" class="form-control" id="announcement-expiry-date">
                                        <div class="form-text">
                                            지정한 날짜 이후에는 공지사항이 표시되지 않습니다.<br>
                                            비워두면 만료일이 없습니다.
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
                            <button type="button" class="btn btn-primary" id="btn-save-announcement">저장</button>
                        </div>
                    </div>
                </div>
            </div>
        `;
    },
    
    // 공지사항 목록 로드
    async loadAnnouncements() {
        console.log('Loading announcements list...');
        try {
            // API 서버 요청 URL 구성
            const apiBaseUrl = window.adminCore && window.adminCore.API_BASE_URL ? 
                window.adminCore.API_BASE_URL : '/api';
            
            // API 경로 구성 - admin 중복 방지
            let apiPath = '/announcements';
            
            // apiBaseUrl이 /api/admin 또는 /api/admin/로 끝나는지 확인
            const hasAdmin = apiBaseUrl.endsWith('/admin') || apiBaseUrl.endsWith('/admin/');
            
            // admin이 없는 경우에만 추가
            if (!hasAdmin) {
                apiPath = '/admin' + apiPath;
            }
            
            console.log(`Loading announcements from: ${apiBaseUrl}${apiPath}`);
            
            // 공지사항 목록 조회 API 요청
            const response = await axios.get(`${apiBaseUrl}${apiPath}`);
            console.log('Announcements API response:', response.data);
            
            // 성공적으로 받아온 데이터 처리
            if (response.data && response.data.data) {
                this.announcements = response.data.data;
                console.log(`Loaded ${this.announcements.length} announcements`);
                
                // 첫 번째 공지사항의 author 정보 상세 확인 (디버깅용)
                if (this.announcements.length > 0) {
                    const firstAnnouncement = this.announcements[0];
                    console.log('공지사항 객체의 모든 키:', Object.keys(firstAnnouncement));
                    console.log('공지사항 객체 전체 내용:', firstAnnouncement);
                    
                    // author 필드 확인
                    if (firstAnnouncement.author) {
                        console.log('author 필드 값:', firstAnnouncement.author);
                        if (typeof firstAnnouncement.author === 'object') {
                            console.log('author의 username:', firstAnnouncement.author.userName || firstAnnouncement.author.username);
                        }
                    } else {
                        console.log('author 필드가 없거나 null입니다');
                    }
                    
                    // 다른 상태 관련 필드 확인
                    console.log('announcementStatus 필드 값:', firstAnnouncement.announcementStatus);
                    console.log('state 필드 값:', firstAnnouncement.state);
                    console.log('state 필드 값:', firstAnnouncement.status);
                    console.log('publishStatus 필드 값:', firstAnnouncement.publishStatus);
                }
                
                // 공지사항 테이블 렌더링
                this.renderAnnouncementsTable();
            } else {
                console.error('Invalid announcements data format:', response.data);
            }
        } catch (error) {
            console.error('공지사항 목록 로드 오류:', error);
            document.getElementById('announcements-table-body').innerHTML = `
                <tr>
                    <td colspan="7" class="text-center text-danger">공지사항 목록을 불러오는 중 오류가 발생했습니다.</td>
                </tr>
            `;
        }
    },
    
    // 공지사항 테이블 렌더링
    renderAnnouncementsTable() {
        console.log('Rendering announcements table...');
        const tableBody = document.getElementById('announcements-table-body');
        
        if (!tableBody) {
            console.error('announcements-table-body element not found');
            return;
        }
        
        if (this.announcements.length === 0) {
            tableBody.innerHTML = `
                <tr>
                    <td colspan="7" class="text-center">등록된 공지사항이 없습니다.</td>
                </tr>
            `;
            return;
        }
        
        let html = '';
        
        console.log('렌더링할 공지사항 데이터:', this.announcements);
        
        // 첫 번째 공지사항의 모든 키를 출력하여 필드명 확인
        if (this.announcements.length > 0) {
            const firstAnnouncement = this.announcements[0];
            console.log('공지사항 객체의 모든 키:', Object.keys(firstAnnouncement));
            console.log('공지사항 객체 전체 내용:', firstAnnouncement);
            
            // 각 필드의 값 확인
            console.log('announcementStatus 필드 값:', firstAnnouncement.announcementStatus);
            console.log('state 필드 값:', firstAnnouncement.state);
            console.log('state 필드 값:', firstAnnouncement.announcementState);
            console.log('publishStatus 필드 값:', firstAnnouncement.publishStatus);
        }
        
        this.announcements.forEach(announcement => {
            // 게시일/만료일 정보 처리
            const publishDate = this.getDateField(announcement, 'publishDate');
            const expiryDate = this.getDateField(announcement, 'expiryDate');
            
            // 공지사항 상태 확인 (현재 시간과 게시일 비교)
            const today = new Date();
            const pubDate = publishDate ? new Date(publishDate) : null;
            console.log(`공지사항 ID ${announcement.id}의 publishDate:`, publishDate);
            const status = !pubDate ? 'DRAFT' : (pubDate > today ? 'DRAFT' : 'PUBLISHED');
            console.log(`공지사항 게시일: ${pubDate}, 현재 시간: ${today}`);
            console.log(`비교 결과: ${status}`);
            
            // 공지사항 개별 상태 확인
            console.log(`공지사항 ID ${announcement.id} 상태:`, status);
            
            // 중요 공지사항 여부에 따라 행 스타일 설정
            const rowClass = announcement.important ? 'table-warning' : '';
            
            html += `
                <tr class="${rowClass}">
                    <td>${announcement.id}</td>
                    <td>
                        ${announcement.important ? '<span class="badge bg-danger me-1">중요</span>' : ''}
                        ${announcement.title || '-'}
                    </td>
                    <td>${this.getAuthorName(announcement)}</td>
                    <td>${this.formatDate(publishDate)}</td>
                    <td>${this.formatDate(expiryDate) || '없음'}</td>
                    <td>
                        <span class="badge ${status === 'PUBLISHED' ? 'bg-success' : 'bg-warning'}">
                            ${status === 'PUBLISHED' ? '게시중' : '임시저장'}
                        </span>
                    </td>
                    <td>
                        <div class="btn-group btn-group-sm" role="group">
                            <button class="btn btn-sm btn-primary edit-announcement" data-id="${announcement.id}" title="공지사항 수정">
                                <i class="bi bi-pencil-square"></i>
                            </button>
                            <button class="btn btn-sm btn-danger delete-announcement" data-id="${announcement.id}" title="공지사항 삭제">
                                <i class="bi bi-trash"></i>
                            </button>
                        </div>
                    </td>
                </tr>
            `;
        });
        
        tableBody.innerHTML = html;
        console.log('Announcements table rendered with', this.announcements.length, 'rows');
        
        // 버튼 이벤트 리스너 추가
        this.addTableButtonListeners();
    },
    
    // 테이블 버튼 이벤트 리스너 추가
    addTableButtonListeners() {
        console.log('Adding table button listeners...');
        
        // 수정 버튼 이벤트 추가
        const editButtons = document.querySelectorAll('.edit-announcement');
        console.log(`Found ${editButtons.length} edit buttons`);
        
        editButtons.forEach(button => {
            button.addEventListener('click', () => {
                const announcementId = button.getAttribute('data-id');
                console.log(`Edit button clicked for announcement ID: ${announcementId}`);
                if (announcementId) {
                    this.showEditAnnouncementModal(announcementId);
                }
            });
        });
        
        // 삭제 버튼 이벤트 추가
        const deleteButtons = document.querySelectorAll('.delete-announcement');
        console.log(`Found ${deleteButtons.length} delete buttons`);
        
        deleteButtons.forEach(button => {
            button.addEventListener('click', () => {
                const announcementId = button.getAttribute('data-id');
                console.log(`Delete button clicked for announcement ID: ${announcementId}`);
                if (announcementId) {
                    this.confirmDeleteAnnouncement(announcementId);
                }
            });
        });
    },
    
    // 이벤트 리스너 설정
    setupEventListeners() {
        // 공지사항 추가 버튼
        const addButton = document.getElementById('btn-add-announcement');
        if (addButton) {
            addButton.addEventListener('click', () => this.showAddAnnouncementModal());
        }
        
        // 공지사항 저장 버튼
        const saveButton = document.getElementById('btn-save-announcement');
        if (saveButton) {
            saveButton.addEventListener('click', () => this.saveAnnouncement());
        }
    },
    
    // 공지사항 추가 모달 표시
    showAddAnnouncementModal() {
        // 모달 타이틀 설정
        document.getElementById('announcementModalLabel').textContent = '공지사항 등록';
        
        // 공지사항 ID 초기화
        document.getElementById('announcement-id').value = '';
        
        // 공지사항 입력 초기화
        document.getElementById('announcement-title').value = '';
        document.getElementById('announcement-content').value = '';
        document.getElementById('announcement-important').checked = false;
        
        // 날짜 설정 초기화
        this.initDatepickers();
        
        // 현재 날짜/시간으로 게시일 초기화
        const now = new Date();
        if (this.publishDatepicker) {
            this.publishDatepicker.setDate(now);
        }
        
        // 모달 표시
        const modal = new bootstrap.Modal(document.getElementById('announcementModal'));
        modal.show();
    },
    
    // 공지사항 수정 모달 표시
    async showEditAnnouncementModal(announcementId) {
        console.log(`Showing edit modal for announcement ID: ${announcementId}`);
        try {
            const announcement = this.announcements.find(a => a.id == announcementId);
            
            if (!announcement) {
                console.error(`ID가 ${announcementId}인 공지사항을 찾을 수 없습니다.`);
                window.adminCore.showModal('오류', `공지사항 정보를 찾을 수 없습니다.`);
                return;
            }
            
            console.log('Announcement data for editing:', announcement);
            
            // 모달 타이틀 변경
            document.getElementById('announcementModalLabel').textContent = '공지사항 수정';
            
            // 폼 필드 값 설정
            document.getElementById('announcement-id').value = announcement.id;
            document.getElementById('announcement-title').value = announcement.title || '';
            document.getElementById('announcement-content').value = announcement.content || '';
            
            // 상태 선택
            const statusSelect = document.getElementById('announcement-status');
            if (statusSelect) {
                const status = this.getAnnouncementStatus(announcement);
                console.log(`공지사항 ID ${announcement.id}의 상태값:`, status);
                statusSelect.value = status;
            }
            
            // 중요 공지사항 체크박스 설정
            const importantCheckbox = document.getElementById('announcement-important');
            if (importantCheckbox) {
                importantCheckbox.checked = announcement.important === true;
                console.log(`공지사항 ID ${announcement.id}의 중요도:`, announcement.important);
            }
            
            // Flatpickr 초기화
            this.initDatepickers();
            
            // 게시일 설정
            if (announcement.publishDate) {
                const publishDate = new Date(announcement.publishDate);
                console.log(`공지사항 ID ${announcement.id}의 게시일:`, publishDate);
                
                // Flatpickr를 사용하여 날짜 설정
                if (this.publishDatepicker) {
                    this.publishDatepicker.setDate(publishDate);
                }
            }
            
            // 만료일 설정
            if (announcement.expiryDate) {
                const expiryDate = new Date(announcement.expiryDate);
                console.log(`공지사항 ID ${announcement.id}의 만료일:`, expiryDate);
                
                // Flatpickr를 사용하여 날짜 설정
                if (this.expiryDatepicker) {
                    this.expiryDatepicker.setDate(expiryDate);
                }
            } else {
                // 만료일이 없는 경우 초기화
                if (this.expiryDatepicker) {
                    this.expiryDatepicker.clear();
                }
            }
            
            // 모달 표시
            const modalElement = document.getElementById('announcementModal');
            if (modalElement) {
                const modal = new bootstrap.Modal(modalElement);
                modal.show();
                console.log('Announcement edit modal displayed');
            } else {
                console.error('announcementModal element not found');
            }
        } catch (error) {
            console.error('공지사항 정보 수정 모달 표시 오류:', error);
            window.adminCore.showModal('오류', `공지사항 정보 수정 오류: ${error.message}`);
        }
    },
    
    // 공지사항 저장 (추가 또는 수정)
    async saveAnnouncement() {
        // 폼 유효성 검사
        const form = document.getElementById('announcement-form');
        if (!form.checkValidity()) {
            form.reportValidity();
            return;
        }
        
        // 폼 데이터 수집
        const announcementId = document.getElementById('announcement-id').value;
        
        // 게시일/만료일 값 가져오기
        const publishDateInput = document.getElementById('announcement-publish-date');
        const expiryDateInput = document.getElementById('announcement-expiry-date');
        
        // 게시일은 필수 값
        if (!publishDateInput || !publishDateInput.value) {
            window.adminCore.showModal('오류', '게시일을 지정해주세요.');
            return;
        }
        
        // 게시일과 만료일 변환
        const publishDate = new Date(publishDateInput.value);
        let expiryDate = null;
        
        if (expiryDateInput && expiryDateInput.value) {
            expiryDate = new Date(expiryDateInput.value);
            
            // 만료일이 게시일보다 이전이면 에러 표시
            if (expiryDate <= publishDate) {
                window.adminCore.showModal('오류', '만료일은 게시일 이후로 설정해야 합니다.');
                return;
            }
            
            // 유효한 만료일이면 ISO 형식으로 변환
            expiryDate = expiryDate.toISOString();
            console.log('사용자 지정 만료일 사용:', expiryDate);
        }
        
        // 게시일 ISO 형식으로 변환
        const publishDateISO = publishDate.toISOString();
        console.log('사용자 지정 게시일 사용:', publishDateISO);
        
        const announcementData = {
            title: document.getElementById('announcement-title').value,
            content: document.getElementById('announcement-content').value,
            publishDate: publishDateISO,
            expiryDate: expiryDate,
            important: document.getElementById('announcement-important').checked
        };
        
        // 디버깅용 로그
        console.log('서버로 보내는 공지사항 데이터:', announcementData);
        
        // API 서버 요청 URL 구성
        const apiBaseUrl = window.adminCore && window.adminCore.API_BASE_URL ? 
            window.adminCore.API_BASE_URL : '/api';
        
        try {
            let response;
            let apiPath;
            
            // apiBaseUrl이 /api/admin 또는 /api/admin/로 끝나는지 확인
            const hasAdmin = apiBaseUrl.endsWith('/admin') || apiBaseUrl.endsWith('/admin/');
            
            if (announcementId) {
                // 공지사항 수정 - PUT
                apiPath = `/announcements/${announcementId}`;
                if (!hasAdmin) {
                    apiPath = '/admin' + apiPath;
                }
                
                console.log(`Updating announcement at: ${apiBaseUrl}${apiPath}`);
                
                // CSRF 토큰 헤더 준비
                const headers = this.getCsrfHeaders();
                
                response = await axios.put(`${apiBaseUrl}${apiPath}`, announcementData, { headers });
                window.adminCore.showModal('알림', '공지사항이 성공적으로 수정되었습니다.');
            } else {
                // 공지사항 추가 - POST
                apiPath = '/announcements';
                if (!hasAdmin) {
                    apiPath = '/admin' + apiPath;
                }
                
                console.log(`Creating announcement at: ${apiBaseUrl}${apiPath}`);
                
                // CSRF 토큰 헤더 준비
                const headers = this.getCsrfHeaders();
                
                response = await axios.post(`${apiBaseUrl}${apiPath}`, announcementData, { headers });
                window.adminCore.showModal('알림', '새 공지사항이 성공적으로 추가되었습니다.');
            }
            
            // 모달 닫기
            bootstrap.Modal.getInstance(document.getElementById('announcementModal')).hide();
            
            // 공지사항 목록 갱신
            await this.loadAnnouncements();
        } catch (error) {
            console.error('공지사항 저장 실패:', error);
            let errorMessage = '공지사항 정보 저장 중 오류가 발생했습니다.';
            
            if (error.response && error.response.data && error.response.data.message) {
                errorMessage = error.response.data.message;
            }
            
            window.adminCore.showModal('오류', errorMessage);
        }
    },
    
    // 공지사항 삭제 확인
    confirmDeleteAnnouncement(announcementId) {
        const announcement = this.announcements.find(a => a.id == announcementId);
        if (!announcement) {
            console.error(`ID가 ${announcementId}인 공지사항을 찾을 수 없습니다.`);
            return;
        }
        
        window.adminCore.showModal(
            '공지사항 삭제 확인',
            `"${announcement.title}" 공지사항을 삭제하시겠습니까?`,
            () => this.deleteAnnouncement(announcementId)
        );
    },
    
    // 공지사항 삭제 실행
    async deleteAnnouncement(announcementId) {
        try {
            console.log(`Deleting announcement ID: ${announcementId}`);
            
            // API 서버 요청 URL 구성
            const apiBaseUrl = window.adminCore && window.adminCore.API_BASE_URL ? 
                window.adminCore.API_BASE_URL : '/api';
            
            // API 경로 구성 - admin 중복 방지
            let apiPath = `/announcements/${announcementId}`;
            
            // apiBaseUrl이 /api/admin 또는 /api/admin/로 끝나는지 확인
            const hasAdmin = apiBaseUrl.endsWith('/admin') || apiBaseUrl.endsWith('/admin/');
            
            // admin이 없는 경우에만 추가
            if (!hasAdmin) {
                apiPath = '/admin' + apiPath;
            }
            
            console.log(`Deleting announcement at: ${apiBaseUrl}${apiPath}`);
            
            // CSRF 토큰 헤더 준비
            const headers = this.getCsrfHeaders();
            
            // DELETE 요청
            const response = await axios.delete(`${apiBaseUrl}${apiPath}`, { headers });
            
            window.adminCore.showModal('삭제 완료', '공지사항이 성공적으로 삭제되었습니다.');
            
            // 공지사항 목록 갱신
            await this.loadAnnouncements();
        } catch (error) {
            console.error(`공지사항 삭제 오류:`, error);
            window.adminCore.showModal('오류', `공지사항 삭제 중 오류가 발생했습니다: ${error.message}`);
        }
    },
    
    // CSRF 토큰 헤더 가져오기
    getCsrfHeaders() {
        let headers = {};
        
        // 로그아웃 폼에서 CSRF 토큰 가져오기
        const csrfTokenLogout = document.getElementById('csrf-token-logout');
        if (csrfTokenLogout && csrfTokenLogout.value) {
            headers['X-CSRF-TOKEN'] = csrfTokenLogout.value;
            console.log('Using CSRF token from logout form');
        } else {
            // 메타 태그에서 CSRF 토큰 가져오기
            const metaToken = document.querySelector('meta[name="_csrf"]');
            const metaHeader = document.querySelector('meta[name="_csrf_header"]');
            
            if (metaToken && metaHeader) {
                const headerName = metaHeader.getAttribute('content');
                headers[headerName] = metaToken.getAttribute('content');
                console.log(`Using meta CSRF token with header ${headerName}`);
            }
        }
        
        return headers;
    },
    
    // 다양한 필드 이름을 처리하는 함수
    // Entity와 DTO 간의 필드명 불일치 문제를 해결
    getDateField(obj, field) {
        if (!obj) return null;
        
        // createdAt vs createAt
        if (field === 'createdAt' && obj.createdAt === undefined && obj.createAt !== undefined) {
            return obj.createAt;
        }
        
        // updatedAt vs modifiedAt
        if (field === 'updatedAt' && obj.updatedAt === undefined && obj.modifiedAt !== undefined) {
            return obj.modifiedAt;
        }
        
        return obj[field];
    },
    
    // 공지사항 상태 값을 처리하는 함수
    // publishDate 값을 통해 게시 상태 판단
    getAnnouncementStatus(announcement) {
        if (!announcement) return 'DRAFT';
        
        // 먼저 직접적인 상태 필드 확인
        if (announcement.status !== undefined) {
            return announcement.status;
        }
        
        // 로그 추가
        console.log(`공지사항 ID ${announcement.id}의 publishDate:`, announcement.publishDate);
        
        // publishDate가 있는지 확인
        if (announcement.publishDate) {
            const publishDate = new Date(announcement.publishDate);
            const now = new Date();
            
            console.log(`공지사항 게시일: ${publishDate}, 현재 시간: ${now}`);
            console.log(`비교 결과: ${publishDate <= now ? '게시됨(과거)' : '게시예정(미래)'}`);
            
            if (publishDate <= now) {
                // 현재 시간보다 과거 또는 현재 시간이면 게시 상태
                return 'PUBLISHED';
            }
        }
        
        // publishDate가 없거나 미래 날짜이면 임시저장 상태
        return 'DRAFT';
    },
    
    // 날짜 포맷 변환
    formatDate(dateString) {
        if (!dateString) return '-';
        
        try {
            const date = new Date(dateString);
            if (isNaN(date.getTime())) {
                console.warn(`Invalid date string: ${dateString}`);
                return dateString; // 유효한 날짜가 아닌 경우 원본 문자열 반환
            }
            
            const formatted = date.toLocaleDateString('ko-KR', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit'
            });
            return formatted;
        } catch (error) {
            console.error(`Date formatting error for: ${dateString}`, error);
            return dateString;
        }
    },
    
    // 작성자 정보 추출
    getAuthorName(announcement) {
        if (!announcement) return '-';
        
        // author 객체가 있는지 확인
        if (!announcement.author) {
            console.log(`공지사항 ID ${announcement.id} author 필드 없음`);
            return '-';
        }
        
        // 객체형태인지 확인
        if (typeof announcement.author !== 'object') {
            console.log(`공지사항 ID ${announcement.id} author가 객체형식이 아님:`, announcement.author);
            return announcement.author.toString() || '-';
        }
        
        // username 필드명 조합을 확인 (userName, username, name 겹침 처리)
        const authorObj = announcement.author;
        const authorName = authorObj.userName || authorObj.username || authorObj.name || '-';
        
        console.log(`공지사항 ID ${announcement.id} 작성자:`, authorName);
        return authorName;
    },
    
    // 날짜를 datetime-local 형식으로 변환 (YYYY-MM-DDThh:mm)
    formatDateForDatetimeLocal(date) {
        if (!date || !(date instanceof Date) || isNaN(date.getTime())) {
            console.warn('Invalid date for datetime-local format:', date);
            return '';
        }
        
        // YYYY-MM-DDThh:mm 형식으로 변환
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        
        return `${year}-${month}-${day}T${hours}:${minutes}`;
    }
};

// 전역 객체에 모듈 등록
window.announcementModule = announcementModule;
