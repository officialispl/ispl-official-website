package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.ui.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import android.widget.Toast
import android.content.Intent
import android.net.Uri


@Composable
fun RegisterScreen(
    viewModel: ISPLViewModel,
    initialForm: String = "Player"
) {
    var activeForm by remember { mutableStateOf(initialForm) } // Player, School, Franchise, Sponsor, Contact

    val isSyncing by viewModel.isSyncing.collectAsState()
    val syncStatusMessage by viewModel.syncMessage.collectAsState()
    val lastRegisteredPlayer by viewModel.lastRegisteredPlayer.collectAsState()
    val successMessageDialog by viewModel.successDialogMessage.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(NavyDeepDusk)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }

            // TITLE BOX
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "ISPL DIRECT PLATFORM REGISTRY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GoldBright,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Submit National Portals Application",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextLight,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // FORM CHIP BAR SELECTOR
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val formTypes = listOf("Player", "School", "Franchise", "Sponsor", "Contact")
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            formTypes.take(3).forEach { type ->
                                FormSelectorChip(
                                    name = if (type == "Franchise") "Franchise" else type,
                                    isActive = activeForm == type,
                                    onSelect = { activeForm = type },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            formTypes.drop(3).forEach { type ->
                                FormSelectorChip(
                                    name = if (type == "Contact") "Contact Us" else type,
                                    isActive = activeForm == type,
                                    onSelect = { activeForm = type },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            // DYNAMIC FORM RENDERING
            item {
                AnimatedContent(
                    targetState = activeForm,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "formTransition"
                ) { form ->
                    FormContainer(form = form, viewModel = viewModel)
                }
            }

            item { Spacer(modifier = Modifier.height(30.dp)) }
        }

        // --- AUTOMATED SYNC LOADING INDICATOR ---
        if (isSyncing) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .border(1.dp, GoldBright, RoundedCornerShape(16.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = GoldBright, strokeWidth = 5.dp)
                        Spacer(modifier = Modifier.height(18.dp))
                        Text(
                            text = "AUTOMATED SYNC ACTIVE",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = GoldBright,
                            letterSpacing = 1.5.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = syncStatusMessage,
                            fontSize = 13.sp,
                            color = SlateTextLight,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Syncing Google Sheets [ID: ispl_s1] & SQLite DB in secure background",
                            fontSize = 10.sp,
                            color = SlateTextMuted,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // --- SUCCESS GRAPHICAL POPUP DIALOG WITH DIGITAL PLAYER CARD ---
        if (successMessageDialog != null) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.82f))
                    .clickable { viewModel.clearSuccessDialog() },
                contentAlignment = Alignment.Center
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .border(1.5.dp, GoldBright, RoundedCornerShape(20.dp))
                        .clickable(enabled = false) {}
                        .padding(2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .background(NavyDeepDusk, RoundedCornerShape(20.dp))
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "🏆 APPLICATION CONFIRMED",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldBright
                            )
                            IconButton(onClick = { viewModel.clearSuccessDialog() }) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = SlateTextLight)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = successMessageDialog ?: "",
                            fontSize = 13.sp,
                            color = SlateTextLight,
                            textAlign = TextAlign.Center,
                            lineHeight = 17.sp
                        )

                        // If player registration success, DISPLAY THE COMPILATION DIGITAL CARD!
                        lastRegisteredPlayer?.let { player ->
                            Spacer(modifier = Modifier.height(16.dp))
                            Divider(color = SlateTextMuted.copy(alpha = 0.2f))
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = "Your Digital ISPL Player License",
                                fontSize = 11.sp,
                                color = GoldBright,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            
                            DigitalPlayerLicenseCard(player = player)
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Button(
                            onClick = { viewModel.clearSuccessDialog() },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldBright),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Acknowledge & Close", color = NavyDeepDusk, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FormSelectorChip(
    name: String,
    isActive: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) GoldBright else NavyCardBlue
        ),
        modifier = modifier
            .height(38.dp)
            .clickable { onSelect() }
            .border(
                1.dp,
                if (isActive) GoldBright else SlateTextMuted.copy(alpha = 0.2f),
                RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isActive) NavyDeepDusk else SlateTextLight
            )
        }
    }
}

@Composable
fun FormContainer(form: String, viewModel: ISPLViewModel) {
    Card(
        colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, NavyLightBlue, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            when (form) {
                "Player" -> PlayerForm(viewModel)
                "School" -> SchoolForm(viewModel)
                "Franchise" -> FranchiseForm(viewModel)
                "Sponsor" -> SponsorForm(viewModel)
                "Contact" -> ContactForm(viewModel)
            }
        }
    }
}

@Composable
fun AttachmentUploader(
    label: String,
    attachedFileName: String?,
    onFileSelected: (name: String, path: String) -> Unit,
    onFileRemoved: () -> Unit,
    hasError: Boolean = false
) {
    var showSourceSelectionDialog by remember { mutableStateOf(false) }
    var uploadProgress by remember { mutableStateOf(0f) }
    var isUploading by remember { mutableStateOf(false) }
    var pendingFileName by remember { mutableStateOf("") }
    var pendingFilePath by remember { mutableStateOf("") }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val fileName = uri.lastPathSegment?.substringAfterLast("/") ?: "device_document.file"
            pendingFileName = fileName
            pendingFilePath = uri.toString()
            isUploading = true
            uploadProgress = 0f
        }
    }

    LaunchedEffect(isUploading) {
        if (isUploading) {
            for (progress in 1..10) {
                delay(120)
                uploadProgress = progress / 10f
            }
            isUploading = false
            onFileSelected(pendingFileName, pendingFilePath)
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = label, fontSize = 11.sp, color = if (hasError) CrimsonStatus else GoldBright, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))

        if (attachedFileName == null && !isUploading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .background(NavyDeepDusk, RoundedCornerShape(8.dp))
                    .border(
                        1.dp,
                        Brush.linearGradient(
                            colors = if (hasError) listOf(CrimsonStatus, CrimsonStatus.copy(alpha = 0.3f))
                            else listOf(GoldBright.copy(alpha = 0.5f), GoldDust.copy(alpha = 0.2f))
                        ),
                        RoundedCornerShape(8.dp)
                    )
                    .clickable { showSourceSelectionDialog = true }
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Attach Document",
                        tint = if (hasError) CrimsonStatus else GoldBright,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Select Document / Photo Proof *",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SlateTextLight
                        )
                        Text(
                            text = "Camera, Gallery, PDF or JPEG up to 10MB",
                            fontSize = 10.sp,
                            color = if (hasError) CrimsonStatus else SlateTextMuted
                        )
                    }
                }
            }
            if (hasError) {
                Text(
                    text = "Please select or simulated upload a required file proof.",
                    color = CrimsonStatus,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                )
            }
        } else if (isUploading) {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyDeepDusk),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(0.5.dp, GoldBright.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Uploading file node: $pendingFileName...", fontSize = 11.sp, color = GoldBright, fontWeight = FontWeight.Bold)
                        Text("${(uploadProgress * 100).toInt()}%", fontSize = 11.sp, color = GoldBright)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { uploadProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(CircleShape),
                        color = GoldBright,
                        trackColor = NavyCardBlue
                    )
                }
            }
        } else {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyDeepDusk),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, EmeraldStatus.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldStatus, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = attachedFileName ?: "", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateTextLight, maxLines = 1)
                            Text(text = "Verified Secure Archive Node", fontSize = 10.sp, color = SlateTextMuted)
                        }
                    }
                    IconButton(onClick = { onFileRemoved() }) {
                        Icon(Icons.Default.Delete, contentDescription = "Remove file", tint = CrimsonStatus, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }

    if (showSourceSelectionDialog) {
        AlertDialog(
            onDismissRequest = { showSourceSelectionDialog = false },
            containerColor = NavyCardBlue,
            title = {
                Text("Select Document Source", color = GoldBright, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Attach an actual device photo or pick a fast preloaded sandbox file.",
                        fontSize = 12.sp,
                        color = SlateTextLight
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = NavyDeepDusk),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showSourceSelectionDialog = false
                                filePickerLauncher.launch("*/*")
                            }
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Share, contentDescription = null, tint = GoldBright)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Choose from Device Storage", fontSize = 12.sp, color = SlateTextLight, fontWeight = FontWeight.Bold)
                                Text("Select from device gallery or documents folder", fontSize = 10.sp, color = SlateTextMuted)
                            }
                        }
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = NavyDeepDusk),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, GoldBright.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Mock-Sandbox Preloaded Presets:", fontSize = 11.sp, color = GoldBright, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            val preloadedFiles = listOf(
                                "avatar_sports_face.jpg" to "Player Profile Photo",
                                "stadium_affidavit_noc.pdf" to "School Location Proof",
                                "networth_incorporation_audit.pdf" to "Corporate Fin Statement",
                                "brand_partner_hi_res.png" to "Corporate Sponsor Logo"
                            )
                            preloadedFiles.forEach { (name, type) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable {
                                            showSourceSelectionDialog = false
                                            pendingFileName = name
                                            pendingFilePath = "content://ispl_preloaded/assets/$name"
                                            isUploading = true
                                        }
                                        .background(NavyCardBlue, RoundedCornerShape(4.dp))
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.List, contentDescription = null, tint = GoldDust, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(name, fontSize = 11.sp, color = SlateTextLight, fontWeight = FontWeight.Bold)
                                    }
                                    Text(
                                        text = type,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        color = GoldBright
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showSourceSelectionDialog = false }) {
                    Text("Cancel", color = SlateTextLight)
                }
            },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.border(1.dp, GoldBright, RoundedCornerShape(16.dp))
        )
    }
}

// --- 1. PLAYER FORM ---
@Composable
fun PlayerForm(viewModel: ISPLViewModel) {
    var name by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var ageCat by remember { mutableStateOf("U14") }
    var school by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("Batsman") }
    var experience by remember { mutableStateOf("") }
    var selectedAvatarId by remember { mutableStateOf(1) }

    // Validation State Boxes
    var nameError by remember { mutableStateOf<String?>(null) }
    var dobError by remember { mutableStateOf<String?>(null) }
    var schoolError by remember { mutableStateOf<String?>(null) }
    var cityError by remember { mutableStateOf<String?>(null) }
    var stateError by remember { mutableStateOf<String?>(null) }
    var mobileError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var photoError by remember { mutableStateOf<String?>(null) }

    var attachedFileName by remember { mutableStateOf<String?>(null) }
    var attachedFilePath by remember { mutableStateOf<String?>(null) }

    val ageOptions = listOf("U14", "U16", "U19")
    val roleOptions = listOf("Batsman", "Bowler", "All-Rounder", "Wicketkeeper")

    Text(text = "🎒 DIGITAL PLAYER PROFILIST DRAFT", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GoldBright)
    Spacer(modifier = Modifier.height(14.dp))

    OutlinedTextField(
        value = name,
        onValueChange = { name = it; nameError = null },
        label = { Text("Player Full Name *") },
        modifier = Modifier.fillMaxWidth(),
        isError = nameError != null,
        supportingText = nameError?.let { { Text(it, color = CrimsonStatus) } },
        colors = outlinedFieldColors()
    )
    Spacer(modifier = Modifier.height(10.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
            value = dob,
            onValueChange = { dob = it; dobError = null },
            label = { Text("DOB * (e.g. 2012-05-14)") },
            modifier = Modifier.weight(1.1f),
            isError = dobError != null,
            supportingText = dobError?.let { { Text(it, color = CrimsonStatus) } },
            colors = outlinedFieldColors()
        )
        Column(modifier = Modifier.weight(0.9f)) {
            Text("Age Category", fontSize = 11.sp, color = SlateTextMuted)
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                ageOptions.forEach { opt ->
                    Box(
                        modifier = Modifier
                            .background(if (ageCat == opt) GoldBright else NavyDeepDusk, RoundedCornerShape(4.dp))
                            .border(1.dp, GoldBright, RoundedCornerShape(4.dp))
                            .weight(1f)
                            .clickable { ageCat = opt }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(opt, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (ageCat == opt) NavyDeepDusk else SlateTextLight)
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
        value = school,
        onValueChange = { school = it; schoolError = null },
        label = { Text("School Name *") },
        modifier = Modifier.fillMaxWidth(),
        isError = schoolError != null,
        supportingText = schoolError?.let { { Text(it, color = CrimsonStatus) } },
        colors = outlinedFieldColors()
    )
    Spacer(modifier = Modifier.height(10.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
            value = city,
            onValueChange = { city = it; cityError = null },
            label = { Text("City *") },
            modifier = Modifier.weight(1f),
            isError = cityError != null,
            supportingText = cityError?.let { { Text(it, color = CrimsonStatus) } },
            colors = outlinedFieldColors()
        )
        OutlinedTextField(
            value = state,
            onValueChange = { state = it; stateError = null },
            label = { Text("State *") },
            modifier = Modifier.weight(1f),
            isError = stateError != null,
            supportingText = stateError?.let { { Text(it, color = CrimsonStatus) } },
            colors = outlinedFieldColors()
        )
    }
    Spacer(modifier = Modifier.height(10.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
            value = mobile,
            onValueChange = { mobile = it; mobileError = null },
            label = { Text("Mobile * (10-Digit)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.weight(1f),
            isError = mobileError != null,
            supportingText = mobileError?.let { { Text(it, color = CrimsonStatus) } },
            colors = outlinedFieldColors()
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it; emailError = null },
            label = { Text("Email *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.weight(1f),
            isError = emailError != null,
            supportingText = emailError?.let { { Text(it, color = CrimsonStatus) } },
            colors = outlinedFieldColors()
        )
    }
    Spacer(modifier = Modifier.height(10.dp))

    Column {
        Text("Playing Role", fontSize = 11.sp, color = SlateTextMuted)
        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            roleOptions.forEach { opt ->
                Box(
                    modifier = Modifier
                        .background(if (role == opt) GoldBright else NavyDeepDusk, RoundedCornerShape(4.dp))
                        .border(1.dp, GoldBright.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                        .weight(1f)
                        .clickable { role = opt }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        opt,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (role == opt) NavyDeepDusk else SlateTextLight,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(14.dp))

    OutlinedTextField(
        value = experience,
        onValueChange = { experience = it },
        label = { Text("Match Achievements & Experience") },
        modifier = Modifier.fillMaxWidth(),
        maxLines = 2,
        colors = outlinedFieldColors()
    )
    Spacer(modifier = Modifier.height(14.dp))

    Text("Select Custom Digital Avatar Profile Picture", fontSize = 11.sp, color = GoldBright, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(6.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..6) {
            val isSelected = selectedAvatarId == i
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(getAvatarColor(i), CircleShape)
                    .border(2.dp, if (isSelected) GoldBright else Color.Transparent, CircleShape)
                    .clickable { selectedAvatarId = i }
                    .shadow(if (isSelected) 4.dp else 0.dp, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(getAvatarEmoji(id = i), fontSize = 18.sp)
            }
        }
    }

    Spacer(modifier = Modifier.height(18.dp))

    AttachmentUploader(
        label = "UPLOAD OFFICIAL COLOR PROFILE PORTRET PHOTO *",
        attachedFileName = attachedFileName,
        onFileSelected = { name, path ->
            attachedFileName = name
            attachedFilePath = path
            photoError = null
        },
        onFileRemoved = {
            attachedFileName = null
            attachedFilePath = null
        },
        hasError = photoError != null
    )

    Spacer(modifier = Modifier.height(18.dp))

    Button(
        onClick = {
            val isNameValid = name.trim().length >= 3
            nameError = if (isNameValid) null else "Name must be at least 3 characters"

            val isDobValid = dob.trim().matches(Regex("^\\d{4}-\\d{2}-\\d{2}$"))
            dobError = if (isDobValid) null else "DOB must be YYYY-MM-DD"

            val isSchoolValid = school.trim().isNotEmpty()
            schoolError = if (isSchoolValid) null else "School name is required"

            val isCityValid = city.trim().isNotEmpty()
            cityError = if (isCityValid) null else "City is required"

            val isStateValid = state.trim().isNotEmpty()
            stateError = if (isStateValid) null else "State is required"

            val isMobileValid = mobile.trim().matches(Regex("^[6-9][0-9]{9}$"))
            mobileError = if (isMobileValid) null else "Must be a 10-digit Indian number"

            val isEmailValid = email.trim().matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
            emailError = if (isEmailValid) null else "Enter a valid email address"

            val isPhotoValid = attachedFileName != null
            photoError = if (isPhotoValid) null else "Required"

            if (isNameValid && isDobValid && isSchoolValid && isCityValid && isStateValid && isMobileValid && isEmailValid && isPhotoValid) {
                viewModel.submitPlayerRegistration(name, dob, ageCat, school, city, state, mobile, email, role, experience, selectedAvatarId, attachedFilePath)
            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = GoldBright),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text("Submit Player & Sync Hub (₹599)", color = NavyDeepDusk, fontWeight = FontWeight.Black, fontSize = 15.sp)
    }
}

// --- 2. SCHOOL FORM ---
@Composable
fun SchoolForm(viewModel: ISPLViewModel) {
    var schoolName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var person by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var studentsCount by remember { mutableStateOf("15") }
    var captainName by remember { mutableStateOf("") }

    // Validation States
    var schoolNameError by remember { mutableStateOf<String?>(null) }
    var addressError by remember { mutableStateOf<String?>(null) }
    var personError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var studentsCountError by remember { mutableStateOf<String?>(null) }
    var captainError by remember { mutableStateOf<String?>(null) }
    var docError by remember { mutableStateOf<String?>(null) }

    var attachedFileName by remember { mutableStateOf<String?>(null) }
    var attachedFilePath by remember { mutableStateOf<String?>(null) }

    Text(text = "🏫 ACCREDITED SCHOOL TEAM REGISTRY", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GoldBright)
    Spacer(modifier = Modifier.height(14.dp))

    OutlinedTextField(
        value = schoolName,
        onValueChange = { schoolName = it; schoolNameError = null },
        label = { Text("School Name *") },
        modifier = Modifier.fillMaxWidth(),
        isError = schoolNameError != null,
        supportingText = schoolNameError?.let { { Text(it, color = CrimsonStatus) } },
        colors = outlinedFieldColors()
    )
    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
        value = address,
        onValueChange = { address = it; addressError = null },
        label = { Text("Official Location Address *") },
        modifier = Modifier.fillMaxWidth(),
        isError = addressError != null,
        supportingText = addressError?.let { { Text(it, color = CrimsonStatus) } },
        colors = outlinedFieldColors()
    )
    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
        value = person,
        onValueChange = { person = it; personError = null },
        label = { Text("Contact Person / HOD Sports *") },
        modifier = Modifier.fillMaxWidth(),
        isError = personError != null,
        supportingText = personError?.let { { Text(it, color = CrimsonStatus) } },
        colors = outlinedFieldColors()
    )
    Spacer(modifier = Modifier.height(10.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it; phoneError = null },
            label = { Text("Phone *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.weight(1f),
            isError = phoneError != null,
            supportingText = phoneError?.let { { Text(it, color = CrimsonStatus) } },
            colors = outlinedFieldColors()
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it; emailError = null },
            label = { Text("School Email *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.weight(1f),
            isError = emailError != null,
            supportingText = emailError?.let { { Text(it, color = CrimsonStatus) } },
            colors = outlinedFieldColors()
        )
    }
    Spacer(modifier = Modifier.height(10.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
            value = studentsCount,
            onValueChange = { studentsCount = it; studentsCountError = null },
            label = { Text("Students Count *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f),
            isError = studentsCountError != null,
            supportingText = studentsCountError?.let { { Text(it, color = CrimsonStatus) } },
            colors = outlinedFieldColors()
        )
        OutlinedTextField(
            value = captainName,
            onValueChange = { captainName = it; captainError = null },
            label = { Text("Proposed Captain *") },
            modifier = Modifier.weight(1f),
            isError = captainError != null,
            supportingText = captainError?.let { { Text(it, color = CrimsonStatus) } },
            colors = outlinedFieldColors()
        )
    }

    Spacer(modifier = Modifier.height(18.dp))

    AttachmentUploader(
        label = "UPLOAD OFFICIAL school NOC Ground Affiliation Agreement *",
        attachedFileName = attachedFileName,
        onFileSelected = { name, path ->
            attachedFileName = name
            attachedFilePath = path
            docError = null
        },
        onFileRemoved = {
            attachedFileName = null
            attachedFilePath = null
        },
        hasError = docError != null
    )

    Spacer(modifier = Modifier.height(18.dp))

    Button(
        onClick = {
            val isSNameValid = schoolName.trim().length >= 5
            schoolNameError = if (isSNameValid) null else "Must be at least 5 characters"

            val isAddrValid = address.trim().length >= 10
            addressError = if (isAddrValid) null else "Must be at least 10 characters"

            val isPersonValid = person.trim().isNotEmpty()
            personError = if (isPersonValid) null else "Required field"

            val isPhoneValid = phone.trim().matches(Regex("^[6-9][0-9]{9}$"))
            phoneError = if (isPhoneValid) null else "Must be a 10-digit Indian number"

            val isEmailValid = email.trim().matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
            emailError = if (isEmailValid) null else "Invalid email"

            val countInt = studentsCount.toIntOrNull() ?: 0
            val isCountValid = countInt in 11..100
            studentsCountError = if (isCountValid) null else "Must be between 11 and 100 students"

            val isCaptValid = captainName.trim().isNotEmpty()
            captainError = if (isCaptValid) null else "Required"

            val isDocValid = attachedFileName != null
            docError = if (isDocValid) null else "Required"

            if (isSNameValid && isAddrValid && isPersonValid && isPhoneValid && isEmailValid && isCountValid && isCaptValid && isDocValid) {
                viewModel.submitSchoolRegistration(schoolName, address, person, phone, email, countInt, captainName, attachedFilePath)
            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = GoldBright),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text("Submit School Onboarding Proposal", color = NavyDeepDusk, fontWeight = FontWeight.Bold)
    }
}

// --- 3. FRANCHISE APPLICATION FORM ---
@Composable
fun FranchiseForm(viewModel: ISPLViewModel) {
    var brandName by remember { mutableStateOf("") }
    var stateTarget by remember { mutableStateOf("") }
    var netWorth by remember { mutableStateOf("₹50 Crore+") }
    var stadium by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }

    var brandNameError by remember { mutableStateOf<String?>(null) }
    var targetStateError by remember { mutableStateOf<String?>(null) }
    var stadiumError by remember { mutableStateOf<String?>(null) }
    var budgetError by remember { mutableStateOf<String?>(null) }
    var docError by remember { mutableStateOf<String?>(null) }

    var attachedFileName by remember { mutableStateOf<String?>(null) }
    var attachedFilePath by remember { mutableStateOf<String?>(null) }

    Text(text = "🏛️ STATE FRANCHISE TRUST BIDDING", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GoldBright)
    Spacer(modifier = Modifier.height(14.dp))

    OutlinedTextField(
        value = brandName,
        onValueChange = { brandName = it; brandNameError = null },
        label = { Text("Corporate Applicant Group Name *") },
        modifier = Modifier.fillMaxWidth(),
        isError = brandNameError != null,
        supportingText = brandNameError?.let { { Text(it, color = CrimsonStatus) } },
        colors = outlinedFieldColors()
    )
    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
        value = stateTarget,
        onValueChange = { stateTarget = it; targetStateError = null },
        label = { Text("Target State franchise * (e.g. Maharashtra Titans)") },
        modifier = Modifier.fillMaxWidth(),
        isError = targetStateError != null,
        supportingText = targetStateError?.let { { Text(it, color = CrimsonStatus) } },
        colors = outlinedFieldColors()
    )
    Spacer(modifier = Modifier.height(10.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
            value = netWorth,
            onValueChange = { netWorth = it },
            label = { Text("Corporate Net Worth") },
            modifier = Modifier.weight(1.2f),
            colors = outlinedFieldColors()
        )
        OutlinedTextField(
            value = budget,
            onValueChange = { budget = it; budgetError = null },
            label = { Text("Committed Budget *") },
            modifier = Modifier.weight(1.1f),
            isError = budgetError != null,
            supportingText = budgetError?.let { { Text(it, color = CrimsonStatus) } },
            colors = outlinedFieldColors()
        )
    }
    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
        value = stadium,
        onValueChange = { stadium = it; stadiumError = null },
        label = { Text("Proposed Local Stadium Venue *") },
        modifier = Modifier.fillMaxWidth(),
        isError = stadiumError != null,
        supportingText = stadiumError?.let { { Text(it, color = CrimsonStatus) } },
        colors = outlinedFieldColors()
    )

    Spacer(modifier = Modifier.height(18.dp))

    AttachmentUploader(
        label = "UPLOAD OFFICIAL CORPORATE BID PITCH OR AUDITED STATEMENT *",
        attachedFileName = attachedFileName,
        onFileSelected = { name, path ->
            attachedFileName = name
            attachedFilePath = path
            docError = null
        },
        onFileRemoved = {
            attachedFileName = null
            attachedFilePath = null
        },
        hasError = docError != null
    )

    Spacer(modifier = Modifier.height(18.dp))

    Button(
        onClick = {
            val isBrandValid = brandName.trim().length >= 3
            brandNameError = if (isBrandValid) null else "Must be at least 3 characters"

            val isStateValid = stateTarget.trim().isNotEmpty()
            targetStateError = if (isStateValid) null else "Target state is required"

            val isStadiumValid = stadium.trim().isNotEmpty()
            stadiumError = if (isStadiumValid) null else "Stadium name is required"

            val isBudgetValid = budget.trim().isNotEmpty()
            budgetError = if (isBudgetValid) null else "Budget bid commitment is required"

            val isDocValid = attachedFileName != null
            docError = if (isDocValid) null else "Required attachment proof"

            if (isBrandValid && isStateValid && isStadiumValid && isBudgetValid && isDocValid) {
                viewModel.submitFranchiseApplication(brandName, stateTarget, netWorth, stadium, budget, attachedFilePath)
            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = GoldBright),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text("Submit State Franchise Tender", color = NavyDeepDusk, fontWeight = FontWeight.Bold)
    }
}

// --- 4. SPONSOR REGISTRATION ---
@Composable
fun SponsorForm(viewModel: ISPLViewModel) {
    var companyName by remember { mutableStateOf("") }
    var brandName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var sponsorType by remember { mutableStateOf("Title Sponsor") }

    var companyNameError by remember { mutableStateOf<String?>(null) }
    var brandNameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var logoError by remember { mutableStateOf<String?>(null) }

    var attachedFileName by remember { mutableStateOf<String?>(null) }
    var attachedFilePath by remember { mutableStateOf<String?>(null) }

    val sponsorTiers = listOf("Title Sponsor (₹5L+)", "Powered By (₹2.5L+)", "Associate (1L+)", "Partner (50k+)")

    Text(text = "🤝 CORPORATE SPONSOR ALLIANCE PARTNERSHIP", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GoldBright)
    Spacer(modifier = Modifier.height(14.dp))

    OutlinedTextField(
        value = companyName,
        onValueChange = { companyName = it; companyNameError = null },
        label = { Text("Company Legal Name *") },
        modifier = Modifier.fillMaxWidth(),
        isError = companyNameError != null,
        supportingText = companyNameError?.let { { Text(it, color = CrimsonStatus) } },
        colors = outlinedFieldColors()
    )
    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
        value = brandName,
        onValueChange = { brandName = it; brandNameError = null },
        label = { Text("Promoted Brand Name *") },
        modifier = Modifier.fillMaxWidth(),
        isError = brandNameError != null,
        supportingText = brandNameError?.let { { Text(it, color = CrimsonStatus) } },
        colors = outlinedFieldColors()
    )
    Spacer(modifier = Modifier.height(10.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it; emailError = null },
            label = { Text("Partnership Email *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.weight(1.1f),
            isError = emailError != null,
            supportingText = emailError?.let { { Text(it, color = CrimsonStatus) } },
            colors = outlinedFieldColors()
        )
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it; phoneError = null },
            label = { Text("Direct Line *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.weight(0.9f),
            isError = phoneError != null,
            supportingText = phoneError?.let { { Text(it, color = CrimsonStatus) } },
            colors = outlinedFieldColors()
        )
    }
    Spacer(modifier = Modifier.height(14.dp))

    Column {
        Text("Requested Sponsor Tier", fontSize = 11.sp, color = SlateTextMuted)
        Spacer(modifier = Modifier.height(3.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            sponsorTiers.take(2).forEach { tier ->
                val pureTier = tier.substringBefore(" (")
                Box(
                    modifier = Modifier
                        .background(if (sponsorType == pureTier) GoldBright else NavyDeepDusk, RoundedCornerShape(4.dp))
                        .border(1.dp, GoldBright.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                        .weight(1f)
                        .clickable { sponsorType = pureTier }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(tier, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (sponsorType == pureTier) NavyDeepDusk else SlateTextLight, maxLines = 1)
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            sponsorTiers.drop(2).forEach { tier ->
                val pureTier = tier.substringBefore(" (")
                Box(
                    modifier = Modifier
                        .background(if (sponsorType == pureTier) GoldBright else NavyDeepDusk, RoundedCornerShape(4.dp))
                        .border(1.dp, GoldBright.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                        .weight(1f)
                        .clickable { sponsorType = pureTier }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(tier, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (sponsorType == pureTier) NavyDeepDusk else SlateTextLight, maxLines = 1)
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(18.dp))

    AttachmentUploader(
        label = "UPLOAD COMPANY BRAND LOGO OR PITCH VECTOR DRAWABLE *",
        attachedFileName = attachedFileName,
        onFileSelected = { name, path ->
            attachedFileName = name
            attachedFilePath = path
            logoError = null
        },
        onFileRemoved = {
            attachedFileName = null
            attachedFilePath = null
        },
        hasError = logoError != null
    )

    Spacer(modifier = Modifier.height(18.dp))

    Button(
        onClick = {
            val isCompValid = companyName.trim().length >= 3
            companyNameError = if (isCompValid) null else "Must be at least 3 characters"

            val isBrandValid = brandName.trim().length >= 2
            brandNameError = if (isBrandValid) null else "Must be at least 2 characters"

            val isEmailValid = email.trim().matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
            emailError = if (isEmailValid) null else "Invalid partnership email"

            val isPhoneValid = phone.trim().matches(Regex("^[6-9][0-9]{9}$"))
            phoneError = if (isPhoneValid) null else "Must be 10-digit Indian number"

            val isLogoValid = attachedFileName != null
            logoError = if (isLogoValid) null else "Required brand logo attachment"

            if (isCompValid && isBrandValid && isEmailValid && isPhoneValid && isLogoValid) {
                viewModel.submitSponsorRegistration(companyName, brandName, email, sponsorType, phone, attachedFilePath)
            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = GoldBright),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text("Acquire Association Alliance Desk", color = NavyDeepDusk, fontWeight = FontWeight.Bold)
    }
}

// --- 5. CONTACT FORM ---
@Composable
fun ContactForm(viewModel: ISPLViewModel) {
    var contactName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    var contactNameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var subjectError by remember { mutableStateOf<String?>(null) }
    var messageError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Text(text = "✉️ DUAL SPORTS HELPDESK INQUIRY", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GoldBright)
    Spacer(modifier = Modifier.height(6.dp))
    Text(
        text = "Fill the ticket form below or connect instantly through our direct channels:",
        fontSize = 11.sp,
        color = SlateTextMuted
    )
    Spacer(modifier = Modifier.height(14.dp))

    // Official Website Card
    Card(
        colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, GoldBright, RoundedCornerShape(12.dp))
            .clickable {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://indianschoolcricketleague.com/"))
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(context, "Opening website: https://indianschoolcricketleague.com/", Toast.LENGTH_SHORT).show()
                }
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("🌐", fontSize = 20.sp)
                Column {
                    Text(
                        text = "OFFICIAL ISPL WEBSITE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GoldBright,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "indianschoolcricketleague.com",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextLight
                    )
                }
            }
            Text(
                text = "VISIT ➔",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = GoldBright
            )
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Direct Instant Connect Row
    Card(
        colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
        modifier = Modifier
            .fillMaxWidth()
            .border(0.5.dp, GoldBright.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "⚡ INSTANT DIRECT CONNECT",
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                color = GoldBright,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // WhatsApp Directly
                Card(
                    colors = CardDefaults.cardColors(containerColor = NavyDeepDusk),
                    modifier = Modifier
                        .weight(1f)
                        .border(0.5.dp, Color(0xFF25D366).copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .clickable {
                            try {
                                clipboardManager.setText(AnnotatedString("+91 91522 34455"))
                                Toast.makeText(context, "Helpline Phone copied!", Toast.LENGTH_SHORT).show()
                                val uri = Uri.parse("https://api.whatsapp.com/send?phone=919152234455&text=Hi%20ISPL%20Admin")
                                context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                            } catch (e: Exception) {
                                Toast.makeText(context, "Copied Helpline: +91 91522 34455", Toast.LENGTH_LONG).show()
                            }
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("💬", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("WhatsApp Helpline", fontSize = 11.sp, color = SlateTextLight, fontWeight = FontWeight.Bold)
                    }
                }

                // Email Directly
                Card(
                    colors = CardDefaults.cardColors(containerColor = NavyDeepDusk),
                    modifier = Modifier
                        .weight(1f)
                        .border(0.5.dp, GoldBright.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .clickable {
                            try {
                                clipboardManager.setText(AnnotatedString("support@isplcricket.in"))
                                Toast.makeText(context, "Support Email copied!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:support@isplcricket.in")
                                    putExtra(Intent.EXTRA_SUBJECT, "Query regarding ISPL Scouting")
                                }
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Email: support@isplcricket.in", Toast.LENGTH_LONG).show()
                            }
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("✉️", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Official Email", fontSize = 11.sp, color = SlateTextLight, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Social media handle reference line
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyDeepDusk, RoundedCornerShape(6.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📸 @OfficialISPL",
                    fontSize = 10.sp,
                    color = SlateTextMuted,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        try {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/OfficialISPL")))
                        } catch (e: Exception) {
                            Toast.makeText(context, "Instagram: @OfficialISPL", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
                Text(
                    text = "▶️ @OfficialISPL",
                    fontSize = 10.sp,
                    color = SlateTextMuted,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        try {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/@OfficialISPL")))
                        } catch (e: Exception) {
                            Toast.makeText(context, "YouTube: @OfficialISPL", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
                Text(
                    text = "🐦 @OfficialISPL",
                    fontSize = 10.sp,
                    color = SlateTextMuted,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        try {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://x.com/OfficialISPL")))
                        } catch (e: Exception) {
                            Toast.makeText(context, "Twitter/X: @OfficialISPL", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))


    OutlinedTextField(
        value = contactName,
        onValueChange = { contactName = it; contactNameError = null },
        label = { Text("Your Name *") },
        modifier = Modifier.fillMaxWidth(),
        isError = contactNameError != null,
        supportingText = contactNameError?.let { { Text(it, color = CrimsonStatus) } },
        colors = outlinedFieldColors()
    )
    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
        value = email,
        onValueChange = { email = it; emailError = null },
        label = { Text("Your Email *") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        modifier = Modifier.fillMaxWidth(),
        isError = emailError != null,
        supportingText = emailError?.let { { Text(it, color = CrimsonStatus) } },
        colors = outlinedFieldColors()
    )
    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
        value = subject,
        onValueChange = { subject = it; subjectError = null },
        label = { Text("Subject / Purpose *") },
        modifier = Modifier.fillMaxWidth(),
        isError = subjectError != null,
        supportingText = subjectError?.let { { Text(it, color = CrimsonStatus) } },
        colors = outlinedFieldColors()
    )
    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
        value = message,
        onValueChange = { message = it; messageError = null },
        label = { Text("Detail Message * (Min 10 chars)") },
        modifier = Modifier.fillMaxWidth(),
        minLines = 3,
        isError = messageError != null,
        supportingText = messageError?.let { { Text(it, color = CrimsonStatus) } },
        colors = outlinedFieldColors()
    )

    Spacer(modifier = Modifier.height(18.dp))

    Button(
        onClick = {
            val isNameValid = contactName.trim().length >= 3
            contactNameError = if (isNameValid) null else "Must be at least 3 characters"

            val isEmailValid = email.trim().matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
            emailError = if (isEmailValid) null else "Invalid email address"

            val isSubValid = subject.trim().length >= 5
            subjectError = if (isSubValid) null else "Must be at least 5 characters"

            val isMsgValid = message.trim().length >= 10
            messageError = if (isMsgValid) null else "Must be at least 10 characters"

            if (isNameValid && isEmailValid && isSubValid && isMsgValid) {
                viewModel.submitContactMessage(contactName, email, subject, message)
            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = GoldBright),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text("Send Secure Help Ticket", color = NavyDeepDusk, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun DigitalPlayerLicenseCard(player: com.example.data.Player) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(NavyCardBlue, Color(0xFF030D22))
                    )
                )
                .border(2.dp, GoldBright, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column {
                // Header of the card
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "INDIAN SCHOOL PREMIER LEAGUE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = GoldBright
                        )
                        Text(
                            "OFFICIAL DIGITAL CARD [SEASON 1]",
                            fontSize = 8.sp,
                            color = SlateTextMuted
                        )
                    }
                    Text("🏏", fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar Left
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .background(getAvatarColor(player.avatarId), CircleShape)
                            .border(1.5.dp, GoldBright, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(getAvatarEmoji(player.avatarId), fontSize = 38.sp)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Player basic meta Right
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = player.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SlateTextLight
                        )
                        Text(
                            text = "School: ${player.school}",
                            fontSize = 10.sp,
                            color = SlateTextMuted,
                            maxLines = 1
                        )
                        Row(modifier = Modifier.padding(top = 4.dp)) {
                            Box(
                                modifier = Modifier
                                    .background(GoldBright.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    player.ageCategory,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldBright
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .background(NavyLightBlue, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    player.playingRole,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SlateTextLight
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Divider(color = SlateTextMuted.copy(alpha = 0.15f))
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("MEMBER ID (UNIQUE)", fontSize = 8.sp, color = SlateTextMuted)
                        Text(
                            "ISPL-S1-${1000 + player.id}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SlateTextLight,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("TREATMENT", fontSize = 8.sp, color = SlateTextMuted)
                        Box(
                            modifier = Modifier
                                .background(EmeraldStatus.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                .border(0.5.dp, EmeraldStatus, RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                "VERIFIED HUB",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldStatus
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("STATE ZONE", fontSize = 8.sp, color = SlateTextMuted)
                        Text(
                            player.state.uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldBright
                        )
                    }
                }
            }
        }
    }
}

// Helper utilities for beautiful simulated avatars. Highly polished detail
fun getAvatarColor(id: Int): Color {
    return when(id) {
        1 -> Color(0xFF1E3A8A)
        2 -> Color(0xFF135A1C)
        3 -> Color(0xFF881337)
        4 -> Color(0xFF0F5A50)
        5 -> Color(0xFF581C87)
        6 -> Color(0xFF701A75)
        else -> Color(0xFF2E2E2E)
    }
}

fun getAvatarEmoji(id: Int): String {
    return when(id) {
        1 -> "🧑‍🚀" // Captain style
        2 -> "🏃" // Allrounder pace
        3 -> "🕶️" // Cool wicketkeeper
        4 -> "🦁" // Aggressive opener
        5 -> "⚡" // Super bowler
        6 -> "🦊" // Smart tactician
        else -> "👤"
    }
}

@Composable
fun outlinedFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = GoldBright,
    unfocusedBorderColor = SlateTextMuted.copy(alpha = 0.5f),
    focusedLabelColor = GoldBright,
    unfocusedLabelColor = SlateTextMuted,
    unfocusedTextColor = SlateTextLight,
    focusedTextColor = SlateTextLight,
    cursorColor = GoldBright,
    errorBorderColor = CrimsonStatus
)
